package project.trendpick_pro.global.basedata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.trendpick_pro.domain.brand.service.BrandService;
import project.trendpick_pro.domain.cart.entity.dto.request.CartItemRequest;
import project.trendpick_pro.domain.cart.service.CartService;
import project.trendpick_pro.domain.category.entity.MainCategory;
import project.trendpick_pro.domain.category.service.MainCategoryService;
import project.trendpick_pro.domain.category.service.SubCategoryService;
import project.trendpick_pro.domain.common.file.CommonFile;
import project.trendpick_pro.domain.member.entity.Member;
import project.trendpick_pro.domain.member.entity.RoleType;
import project.trendpick_pro.domain.member.entity.dto.MemberInfoDto;
import project.trendpick_pro.domain.member.entity.form.JoinForm;
import project.trendpick_pro.domain.member.repository.MemberRepository;
import project.trendpick_pro.domain.member.service.MemberService;
import project.trendpick_pro.domain.orders.entity.dto.request.OrderForm;
import project.trendpick_pro.domain.orders.entity.dto.response.OrderItemDto;
import project.trendpick_pro.domain.orders.service.OrderService;
import project.trendpick_pro.domain.product.entity.Product;
import project.trendpick_pro.domain.product.repository.ProductRepository;
import project.trendpick_pro.domain.product.service.ProductService;
import project.trendpick_pro.domain.recommend.service.RecommendService;
import project.trendpick_pro.domain.review.entity.Review;
import project.trendpick_pro.domain.review.entity.dto.request.ReviewSaveRequest;
import project.trendpick_pro.domain.review.repository.ReviewRepository;
import project.trendpick_pro.domain.tags.favoritetag.entity.FavoriteTag;
import project.trendpick_pro.domain.tags.tag.entity.Tag;
import project.trendpick_pro.domain.tags.tag.entity.type.TagType;
import project.trendpick_pro.global.basedata.tagname.entity.TagName;
import project.trendpick_pro.global.basedata.tagname.service.TagNameService;
import project.trendpick_pro.global.rsData.RsData;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Configuration
@Profile({"dev", "test"})
public class BaseData {
    @Value("${file.path}")
    private String filePath;
    @Value("${tag}")
    private List<String> tags;
    @Value("${main-category}")
    private List<String> mainCategories;
    @Value("${top}")
    private List<String> tops;
    @Value("${outer}")
    private List<String> outers;
    @Value("${bottom}")
    private List<String> bottoms;
    @Value("${shoes}")
    private List<String> shoes;
    @Value("${bag}")
    private List<String> bags;
    @Value("${accessory}")
    private List<String> accessories;
    @Value("${brand}")
    private List<String> brands;
    @Bean
    CommandLineRunner initData(
            TagNameService tagNameService,
            MemberService memberService,
            MainCategoryService mainCategoryService,
            SubCategoryService subCategoryService,
            BrandService brandService,
            ProductRepository productRepository,
            OrderService orderservice,
            CartService cartService,
            RecommendService recommendService,
            ReviewRepository reviewRepository
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                //원활한 테스트를 위해 잠깐 주석처리
                for (String tag : tags) {
                    tagNameService.save(tag);
                }
                for (String mainCategory : mainCategories) {
                    mainCategoryService.save(mainCategory);
                }
                CreateSubCategories(mainCategoryService, subCategoryService);
//                for (String brand : brands) {
//                    brandService.save(brand);
//                }
                JoinForm admin = JoinForm.builder()
                        .email("admin@naver.com")
                        .password("12345")
                        .username("admin")
                        .phoneNumber("010-1234-1234")
                        .state("ADMIN")
                        .build();
                JoinForm brand_admin1 = JoinForm.builder()
                        .email("brand@naver.com")
                        .password("12345")
                        .username("brand")
                        .phoneNumber("010-1234-1234")
                        .state("BRAND_ADMIN")
                        .brand("나이키")
                        .build();
                JoinForm brand_admin2 = JoinForm.builder()
                        .email("brand2@naver.com")
                        .password("12345")
                        .username("brand2")
                        .phoneNumber("010-1234-1234")
                        .state("BRAND_ADMIN")
                        .brand("아디다스")
                        .build();
                JoinForm brand_admin3 = JoinForm.builder()
                        .email("brand3@naver.com")
                        .password("12345")
                        .username("brand3")
                        .phoneNumber("010-1234-1234")
                        .state("BRAND_ADMIN")
                        .brand("버버리")
                        .build();
                JoinForm member1 = JoinForm.builder()
                        .email("trendpick@naver.com")
                        .password("12345")
                        .username("sooho")
                        .phoneNumber("010-1234-1234")
                        .state("MEMBER")
                        .tags(List.of("오버핏청바지", "로맨틱룩"))
                        .build();
                JoinForm member2 = JoinForm.builder()
                        .email("hye_0000@naver.com")
                        .password("12345")
                        .username("hye0000")
                        .phoneNumber("010-1234-1234")
                        .state("MEMBER")
                        .tags(List.of("오버핏청바지", "로맨틱룩"))
                        .build();
                memberService.register(admin);

                memberService.register(brand_admin1);
                memberService.register(brand_admin2);
                memberService.register(brand_admin3);

                Member Rsmember1 = memberService.register(member1).getData();
                Rsmember1.connectAddress("서울특별시 어디구 어디로 123");
                Member Rsmember2 = memberService.register(member2).getData();
                Rsmember2.connectAddress("경기도 어디구 어디로 456");

                //==상품데이터==//
                for (int n = 1; n <= 10; n++) {
                    CommonFile mainFile = CommonFile.builder()
                            .fileName("bamin.png")
                            .build();
                    List<CommonFile> subFiles = new ArrayList<>();
                    subFiles.add(CommonFile.builder()
                            .fileName("dev-jeans.png")
                            .build());
                    for (CommonFile subFile : subFiles) {
                        mainFile.connectFile(subFile);
                    }
                    Product product = Product
                            .builder()
                            .name("멋쟁이 티셔츠" + n)
                            .description("이 상품은 멋쟁이 티셔츠입니다." + n)
                            .stock(50 + n)
                            .price(20000 + n)
                            .mainCategory(mainCategoryService.findByName("상의"))
                            .subCategory(subCategoryService.findByName("반소매티셔츠"))
                            .brand(brandService.findByName("나이키"))
                            .file(mainFile)
                            .build();
                    Set<Tag> tags = new LinkedHashSet<>();  // 상품에 포함시킬 태크 선택하여 저장
                    for (int i = 1; i <= 5; i++) {
                        TagName tagName = tagNameService.findById(Long.valueOf(i + n));
                        tags.add(new Tag(tagName.getName()));
                    }
                    product.addTag(tags);
                    productRepository.save(product);
                }
                //==장바구니 데이터==//
                cartService.addItemToCart(memberService.findByEmail("trendpick@naver.com").get(),  new CartItemRequest(1L,5));
                cartService.addItemToCart(memberService.findByEmail("trendpick@naver.com").get(), new CartItemRequest(2L,3));
                cartService.addItemToCart(memberService.findByEmail("trendpick@naver.com").get(), new CartItemRequest(3L,1));
                //==주문데이터==//
                Member findMember = memberService.findByEmail("hye_0000@naver.com").get();
                MemberInfoDto memberInfo = new MemberInfoDto(findMember);
                List<OrderItemDto> orderItems = new ArrayList<>();
                orderItems.add(OrderItemDto.of(productRepository.findById(1L).get(), 5));
                orderItems.add(OrderItemDto.of(productRepository.findById(2L).get(), 3));
                orderItems.add(OrderItemDto.of(productRepository.findById(3L).get(), 2));
                OrderForm orderForm = new OrderForm(memberInfo, orderItems);
                orderForm.setPaymentMethod("신용카드");
                orderservice.order(findMember, orderForm);
                //==주문데이터2==//
                Member findMember2 = memberService.findByEmail("trendpick@naver.com").get();
                MemberInfoDto memberInfo2 = new MemberInfoDto(findMember2);
                List<OrderItemDto> orderItems2 = new ArrayList<>();
                orderItems2.add(OrderItemDto.of(productRepository.findById(1L).get(), 5));
                orderItems2.add(OrderItemDto.of(productRepository.findById(2L).get(), 3));
                orderItems2.add(OrderItemDto.of(productRepository.findById(3L).get(), 2));
                OrderForm orderForm2 = new OrderForm(memberInfo2, orderItems2);
                orderForm2.setPaymentMethod("신용카드");
                orderservice.order(findMember2, orderForm2);
                recommendService.select(member1.email());
                //==리뷰데이터==//
                CommonFile mainFile = CommonFile.builder()
                        .fileName("bamin.png")
                        .build();
                List<CommonFile> subFiles = new ArrayList<>();
                subFiles.add(CommonFile.builder()
                        .fileName("dev-jeans.png")
                        .build());
                for (CommonFile subFile : subFiles) {
                    mainFile.connectFile(subFile);
                }
                Product product = productRepository.findById(1L).orElseThrow();
                Product product2 = productRepository.findById(2L).orElseThrow();
                ReviewSaveRequest rr = ReviewSaveRequest.builder()
                        .title("리뷰입니다.")
                        .content("내용입니다")
                        .rating(5)
                        .build();
                Review review = Review.of(rr, memberService.findByEmail("trendpick@naver.com").get(), product, mainFile);
                reviewRepository.save(review);
                Review review2 = Review.of (rr, memberService.findByEmail("hye_0000@naver.com").get(), product, mainFile);
                reviewRepository.save(review2);
                Review review3 = Review.of(rr, memberService.findByEmail("trendpick@naver.com").get(), product2, mainFile);
                reviewRepository.save(review3);
                Review review4 = Review.of (rr, memberService.findByEmail("hye_0000@naver.com").get(), product2, mainFile);
                reviewRepository.save(review4);
            }
        };
    }
    private void CreateSubCategories(MainCategoryService mainCategoryService, SubCategoryService subCategoryService) {
        for (String top : tops) {
            MainCategory mainCategory = mainCategoryService.findByName("상의");
            subCategoryService.save(top, mainCategory);
        }
        for (String outer : outers) {
            MainCategory mainCategory = mainCategoryService.findByName("아우터");
            subCategoryService.save(outer, mainCategory);
        }
        for (String bottom : bottoms) {
            MainCategory mainCategory = mainCategoryService.findByName("하의");
            subCategoryService.save(bottom, mainCategory);
        }
        for (String shoe : shoes) {
            MainCategory mainCategory = mainCategoryService.findByName("신발");
            subCategoryService.save(shoe, mainCategory);
        }
        for (String bag : bags) {
            MainCategory mainCategory = mainCategoryService.findByName("가방");
            subCategoryService.save(bag, mainCategory);
        }
        for (String accessory : accessories) {
            MainCategory mainCategory = mainCategoryService.findByName("악세서리");
            subCategoryService.save(accessory, mainCategory);
        }
    }
}
