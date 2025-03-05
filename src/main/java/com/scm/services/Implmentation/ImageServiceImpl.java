package com.scm.services.Implmentation;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage){
        if (contactImage.isEmpty()) {
            return null; // Return null if no image is uploaded
        }

        String filename = UUID.randomUUID().toString(); // Generate unique filename
        try {
            var uploadResult = cloudinary.uploader().upload(
                contactImage.getBytes(),
                ObjectUtils.asMap("public_id", filename)
            );

            
            // Get the uploaded image URL from Cloudinary
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary
            .url()
            .transformation(
                new Transformation<>()
                    .width(AppConstants.CONTACT_IMAGE_WIDTH)
                    .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                    .crop(AppConstants.CONTACT_IMAGE_CROP)
            )
            .generate(publicId);
    }
}
