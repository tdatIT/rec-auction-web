package com.ec.recauctionec.dto;

import com.ec.recauctionec.entities.ProductImg;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Data
public class ProductDTO {

    private int productId;

    private int categoryId;

    private String detail;

    private String productName;

    private int status;
    private String subDetail;
    private double defaultPrice;

    private double minPrice;

    private int supplierId;
    private String productTag;

    private MultipartFile[] images_file;

    private Collection<ProductImg> images;
    private boolean isDeleted;

}
