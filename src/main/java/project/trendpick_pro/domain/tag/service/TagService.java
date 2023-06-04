package project.trendpick_pro.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.trendpick_pro.domain.favoritetag.entity.FavoriteTag;
import project.trendpick_pro.domain.member.entity.Member;
import project.trendpick_pro.domain.product.entity.Product;
import project.trendpick_pro.domain.tag.entity.Tag;
import project.trendpick_pro.domain.tag.entity.type.TagType;
import project.trendpick_pro.domain.tag.repository.TagRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public void save(String name) {
        tagRepository.save(new Tag(name));
    }

    public void save(String name, Product product) {
        tagRepository.save(new Tag(product, name));
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public void updateTag(Member member, Product product, TagType type) {
        Set<Tag> tagList = product.getTags();
        Set<FavoriteTag> tags = member.getTags();

        for(Tag tagByProduct : tagList){
            boolean hasTag = false;
            for(FavoriteTag tagByMember : tags){
                if(tagByProduct.getName().equals(tagByMember.getName())){ //기존에 가지고 있던 태그에는 점수 부여
                    tagByMember.increaseScore(type);
                    hasTag = true;
                    break;
                }
            }
            if(!hasTag){ //태그를 가지고 있지 않다면 추가해준다.
                FavoriteTag favoriteTag = new FavoriteTag(member, tagByProduct.getName());
                favoriteTag.increaseScore(type);
                tags.add(favoriteTag);
            }
        }
    }
}
