package com.klpdapp.klpd.Services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.klpdapp.klpd.Repository.ImagesRepo;
import com.klpdapp.klpd.model.Images;
import com.klpdapp.klpd.model.Product;

@Service
public class ImageService {

    @Autowired
    ImagesRepo imgRepo;

    public void handleImageUploads(Product product, MultipartFile primaryImage,
			MultipartFile[] secondaryImages, List<Integer> removedImages) {
		List<Images> images = new ArrayList<>();

		// Handle primary image
		if (primaryImage != null && !primaryImage.isEmpty()) {
			String primaryImageUrl = product.getPid() + "_primaryImage_" + primaryImage.getOriginalFilename();
			String uploadDir = "public/ProductImages/";
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Images primaryImg = new Images();
			primaryImg.setImageUrl(primaryImageUrl);
			primaryImg.setIsPrimary(true);
			images.add(primaryImg);
		} else if (product.getImages() != null) {
			// Keep existing primary image if not changed
			product.getImages().stream()
					.filter(Images::getIsPrimary)
					.findFirst()
					.ifPresent(images::add);
		}

		// Handle secondary images
		if (secondaryImages != null) {
			for (MultipartFile file : secondaryImages) {
				if (!file.isEmpty()) {
					String imageUrl = product.getPid() + "_secondaryImage_" + file.getOriginalFilename();
					String uploadDir = "public/ProductImages/";
					Path uploadPath = Paths.get(uploadDir);
					if (!Files.exists(uploadPath)) {
						try {
							Files.createDirectories(uploadPath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Images img = new Images();
					img.setImageUrl(imageUrl);
					img.setIsPrimary(false);
					images.add(img);
				}
			}
		}

		// Add existing secondary images that weren't removed
		if (product.getImages() != null) {
			product.getImages().stream()
					.filter(img -> !img.getIsPrimary())
					.filter(img -> removedImages == null ||
							!removedImages.contains(img.getImgId()))
					.forEach(images::add);
		}

		product.setImages(images);
	}
	public void updateProductImages(Product product, List<Images> updatedImages) {
        if (updatedImages == null || updatedImages.isEmpty()) {
            return;
        }
        
        // Set product reference for all images
        updatedImages.forEach(img -> img.setpid(product));
        
        // Save all images (new ones will be inserted, existing ones updated)
        imgRepo.saveAll(updatedImages);
        
        // Delete images that are no longer referenced
        List<Integer> currentImageIds = updatedImages.stream()
                .map(Images::getImgId)
                .filter(id -> id != 0)
                .collect(Collectors.toList());
        
        if (!currentImageIds.isEmpty()) {
            //imgRepo.deleteByPidAndImgIdNotIn(product, currentImageIds);
        } else {
            imgRepo.deleteByPid(product);
        }
    }

     private final String uploadDir = "public/ProductImages/";

   public void saveImage(MultipartFile file, Product product, boolean isPrimary) throws IOException {
        if (file == null || file.isEmpty()) return;

        String type = isPrimary ? "primaryImage" : "secondaryImage";
        String fileName = product.getPid() + "_" + type + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        Images img = new Images();
        img.setpid(product);
        img.setImageUrl("/ProductImages/" + fileName);
        img.setIsPrimary(isPrimary);
        imgRepo.save(img);
    }


}
