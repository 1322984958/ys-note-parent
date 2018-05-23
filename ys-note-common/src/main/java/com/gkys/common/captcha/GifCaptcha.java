package com.gkys.common.captcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class GifCaptcha extends Captcha {
	public GifCaptcha() {
	}

	public GifCaptcha(int width, int height) {
		this.width = width;
		this.height = height;
		alphas();
	}

	public GifCaptcha(int width, int height, int len) {
		this(width, height);
		this.len = len;
	}

	public GifCaptcha(int width, int height, int len, Font font) {
		this(width, height, len);
		this.font = font;
	}

	public void out(OutputStream os) {
		try {
			GifEncoder gifEncoder = new GifEncoder();

			gifEncoder.start(os);
			gifEncoder.setQuality(180);
			gifEncoder.setDelay(100);
			gifEncoder.setRepeat(0);

			char[] rands = textChar();
			Color[] fontcolor = new Color[this.len];
			for (int i = 0; i < this.len; i++) {
				fontcolor[i] = new Color(20 + num(110), 20 + num(110), 20 + num(110));
			}
			for (int i = 0; i < this.len; i++) {
				BufferedImage frame = graphicsImage(fontcolor, rands, i);
				gifEncoder.addFrame(frame);
				frame.flush();
			}
			gifEncoder.finish();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private BufferedImage graphicsImage(Color[] fontcolor, char[] rands, int flag) {
		BufferedImage image = new BufferedImage(this.width, this.height, 1);

		Graphics2D g2d = (Graphics2D) image.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.width, this.height);

		int h = this.height - (this.height - this.font.getSize() >> 1);
		int w = this.width / this.len;
		g2d.setFont(this.font);
		for (int i = 0; i < this.len; i++) {
			AlphaComposite ac3 = AlphaComposite.getInstance(3, getAlpha(flag, i));
			g2d.setComposite(ac3);
			g2d.setColor(fontcolor[i]);
			g2d.drawOval(num(this.width), num(this.height), 5 + num(10), 5 + num(10));
			g2d.drawString(String.valueOf(rands[i]), this.width - (this.len - i) * w + (w - this.font.getSize()) + 1, h - 4);
		}
		g2d.dispose();
		return image;
	}

	private float getAlpha(int i, int j) {
		int num = i + j;
		float r = 1.0F / this.len;
		float s = (this.len + 1) * r;
		return num > this.len ? num * r - s : num * r;
	}
}
