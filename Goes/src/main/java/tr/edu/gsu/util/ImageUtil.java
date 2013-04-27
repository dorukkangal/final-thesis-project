package tr.edu.gsu.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.StreamResource;

public class ImageUtil {
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static StreamResource getImageSource(byte[] imageAsBytes) {
		if (imageAsBytes == null)
			return null;
		return getImageSource(getBufferedImage(imageAsBytes));
	}

	public static StreamResource resizeImage(byte[] imageAsBytes, int width, int height, boolean keepAspectRatio) {
		if (imageAsBytes == null)
			return null;
		BufferedImage bufferedImage = getBufferedImage(imageAsBytes);
		int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();

		BufferedImage scaleImage = (keepAspectRatio) ? scaleImage(bufferedImage, type, width, height) : drawImage(bufferedImage, type, width, height);
		;
		return getImageSource(scaleImage);
	}

	public static StreamResource resizeImage(byte[] imageAsBytes, int size, boolean keepAspectRatio) {
		return resizeImage(imageAsBytes, size, size, keepAspectRatio);
	}

	public static BufferedImage scaleImage(BufferedImage image, int imageType, int newWidth, int newHeight) {
		double thumbRatio = (double) newWidth / (double) newHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double aspectRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < aspectRatio) {
			newHeight = (int) (newWidth / aspectRatio);
		} else {
			newWidth = (int) (newHeight * aspectRatio);
		}
		return drawImage(image, imageType, newWidth, newHeight);
	}

	private static BufferedImage drawImage(BufferedImage image, int imageType, int width, int height) {
		// Draw the scaled image
		BufferedImage newImage = new BufferedImage(width, height, imageType);
		Graphics2D graphics2D = newImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);

		return newImage;
	}

	private static StreamResource getImageSource(final BufferedImage image) {
		return new StreamResource(new StreamResource.StreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				try {
					ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
					ImageIO.write(image, "png", imagebuffer);

					return new ByteArrayInputStream(imagebuffer.toByteArray());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
					return null;
				}
			}
		}, generateFileName(), AppContext.getApp());
	}

	private static BufferedImage getBufferedImage(byte[] imageAsBytes) {
		try {
			if (imageAsBytes != null)
				return ImageIO.read(new ByteArrayInputStream(imageAsBytes));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}

	private static SecureRandom random = new SecureRandom();

	private static String generateFileName() {
		long n = random.nextLong();
		return "image" + Long.toString(n) + ".png";
	}
}