package org.opentides.util.imageutil;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;

public class Image {
	BufferedImage img;
	ImageType sourceType = ImageType.UNKNOWN;

	Image(InputStream input, ImageType sourceType) throws IOException {
		this.img = ImageIO.read(input);
		input.close();
		this.sourceType = ((sourceType == null) ? ImageType.UNKNOWN
				: sourceType);
	}

	private Image(BufferedImage img, ImageType sourceType) {
		this.img = img;
		this.sourceType = ((sourceType == null) ? ImageType.UNKNOWN
				: sourceType);
	}

	public ImageType getSourceType() {
		return this.sourceType;
	}

	public int getWidth() {
		return this.img.getWidth();
	}

	public int getHeight() {
		return this.img.getHeight();
	}

	public double getAspectRatio() {
		return (getWidth() / getHeight());
	}

	public Image getResizedToWidth(int width) {
		if (width > getWidth())
			throw new IllegalArgumentException("Width " + width
					+ " exceeds width of image, which is " + getWidth());
		int nHeight = width * this.img.getHeight() / this.img.getWidth();
		MultiStepRescaleOp rescale = new MultiStepRescaleOp(width, nHeight);
		rescale.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
		BufferedImage resizedImage = rescale.filter(this.img, null);

		return new Image(resizedImage, this.sourceType);
	}

	public Image crop(int x1, int y1, int x2, int y2) {
		if ((x1 < 0) || (x2 <= x1) || (y1 < 0) || (y2 <= y1)
				|| (x2 > getWidth()) || (y2 > getHeight())) {
			throw new IllegalArgumentException("invalid crop coordinates");
		}
		int type = (this.img.getType() == 0) ? 2 : this.img.getType();
		int nNewWidth = x2 - x1;
		int nNewHeight = y2 - y1;
		BufferedImage cropped = new BufferedImage(nNewWidth, nNewHeight, type);
		Graphics2D g = cropped.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setComposite(AlphaComposite.Src);

		g.drawImage(this.img, 0, 0, nNewWidth, nNewHeight, x1, y1, x2, y2, null);
		g.dispose();

		return new Image(cropped, this.sourceType);
	}

	public Image getResizedToSquare(int width, double cropEdgesPct) {
		if ((cropEdgesPct < 0.0D) || (cropEdgesPct > 0.5D))
			throw new IllegalArgumentException(
					"Crop edges pct must be between 0 and 0.5. " + cropEdgesPct
							+ " was supplied.");
		if (width > getWidth()) {
			throw new IllegalArgumentException("Width " + width
					+ " exceeds width of image, which is " + getWidth());
		}
		int cropMargin = (int) Math
				.abs(Math.round((this.img.getWidth() - this.img.getHeight()) / 2.0D));
		int x1 = 0;
		int y1 = 0;
		int x2 = getWidth();
		int y2 = getHeight();
		if (getWidth() > getHeight()) {
			x1 = cropMargin;
			x2 = x1 + y2;
		} else {
			y1 = cropMargin;
			y2 = y1 + x2;
		}

		if (cropEdgesPct != 0.0D) {
			int cropEdgeAmt = (int) ((x2 - x1) * cropEdgesPct);
			x1 += cropEdgeAmt;
			x2 -= cropEdgeAmt;
			y1 += cropEdgeAmt;
			y2 -= cropEdgeAmt;
		}

		Image cropped = crop(x1, y1, x2, y2);

		Image resized = cropped.getResizedToWidth(width);
		cropped.dispose();

		return resized;
	}

	public Image soften(float softenFactor) {
		if (softenFactor == 0.0F) {
			return this;
		}
		float[] softenArray = { 0.0F, softenFactor, 0.0F, softenFactor,
				1.0F - (softenFactor * 4.0F), softenFactor, 0.0F, softenFactor,
				0.0F };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, 1, null);
		return new Image(cOp.filter(this.img, null), this.sourceType);
	}

	public File writeToFile(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("File argument was null");
		}
		File writeto = null;

		String name = file.getName();
		String ext = null;
		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			ext = name.substring(dot + 1).toLowerCase();
		}

		if (ext != null) {
			if (Arrays.asList(getWriterFormatNames()).contains(ext)) {
				writeto = file;
			} else {
				ext = "jpg";
				writeto = new File(file.getPath() + ".jpg");
			}

		} else if (Arrays.asList(getWriterFormatNames()).contains(
				getSourceType().toString().toLowerCase())) {
			ext = getSourceType().toString().toLowerCase();
			writeto = new File(file.getPath() + "."
					+ getSourceType().toString().toLowerCase());
		} else {
			ext = "jpg";
			writeto = new File(file.getPath() + ".jpg");
		}

		writeToFile(writeto, ext);
		return writeto;
	}

	public void writeToFile(File file, String type) throws IOException {
		if (file == null)
			throw new IllegalArgumentException("File argument was null");
		ImageIO.write(this.img, type, file);
	}

	public String[] getWriterFormatNames() {
		return ImageIO.getWriterFormatNames();
	}

	// TODO Find a way to implement this without using com.sun.image.codec.jpeg packages.
	// sun packages will not work on all platforms. 
	// More info here: http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html
	/*public void writeToJPG(File file, float quality) throws IOException {
		FileOutputStream out = new FileOutputStream(file);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(this.img);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(this.img);
	}*/

	public void dispose() {
		this.img.flush();
	}

	private BufferedImage getBufferedImage() {
		return this.img;
	}
}