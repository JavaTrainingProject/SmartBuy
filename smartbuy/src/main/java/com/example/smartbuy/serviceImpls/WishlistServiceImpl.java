package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.WishlistResponseDto;
import com.example.smartbuy.entity.ProductEntity;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.entity.Wishlist;
import com.example.smartbuy.mapper.WishlistMapper;
import com.example.smartbuy.repository.ProductRepository;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.repository.WishlistRepository;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;




@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public String addToWishlist(Long product_id, String email){
        UserEntity user=userRepository.findByEmail(email)
                .orElseThrow(() ->  new RuntimeException("User not found"));
        ProductEntity product= productRepository.findById(product_id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        boolean exists = wishlistRepository.existsByUserAndProduct(user, product);

        if(exists){
            return "Product already exists in wishlist";
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

        return "Product added to wishlist successfully";

    }


    public String removeFromWishlist(Long wishlistId){

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);

        return "Product removed from wishlist successfully";
    }

    public Page<WishlistResponseDto> getWishlistById(Long user_id, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Wishlist> wishlistPage = wishlistRepository.findByUser_IdOrderByIdDesc(user_id,pageable);
        return wishlistPage.map(WishlistMapper::toDto);

    }

    public boolean isProductInWishlist(Long product_id, String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductEntity product = productRepository.findById(product_id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return wishlistRepository.existsByUserAndProduct(user,product);
    }
}
