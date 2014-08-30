package iris;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends Canvas {

	private static final long serialVersionUID = 1L;
	
	private final int width;
	private final int height;
	private String windowTitle;
	
	private InputHandler input;
	
	private BufferedImage image;
	private int[] pixels;
	private Bitmap frameBuffer;
	
	public Display(int width, int height, String windowTitle) {
		this.width = width;
		this.height = height;
		this.windowTitle = windowTitle;
		
		input = new InputHandler();
		this.addKeyListener(input);
		
		Dimension size = new Dimension(this.width, this.height);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		
		image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		frameBuffer = new Bitmap(this.width, this.height);
		
		this.createWindow(this.windowTitle);
	}
	
	public void createWindow(String title) {
		JFrame frame = new JFrame(title);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		for(int i = 0; i < this.getWidth() * this.getHeight(); ++i) {
			pixels[i] = frameBuffer.getPixel(i);
		}
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
		g.dispose();
		bs.show();
	} 	
	
	public void swapBuffers() { 
		this.render();
	}
	
	public void setPixel(int x, int y, int color) { frameBuffer.setPixel(x, y, color); }
	
	public int getPixel(int x, int y) { return frameBuffer.getPixel(x, y); }
	
	public boolean[] getKeys() { return input.keys; }
	
	public void clear() { frameBuffer.fill(0xFF000000); }
	public void clear(int color) { frameBuffer.fill(color); }
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public String getWindowTitle() { return this.windowTitle; }
	
	public Bitmap getFrameBuffer() { return this.frameBuffer; }
	
	public double getHalfWidth() { return this.width / 2.0; }
	public double getHalfHeight() { return this.height / 2.0; }
	
	public int getXCenter() { return this.width / 2; }
	public int getYCenter() { return this.height / 2; }
}
