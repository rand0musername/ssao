package ssao;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.GL_RGB16F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import rafgl.RGL;

public class FrameBufferObject {
	// Channels: Color - Normal - Position

	private int bufferHandle;
	private int colorTex;
	private int normalTex;
	private int positionTex;

	public FrameBufferObject(boolean G, int colorTex) {
		this.colorTex = colorTex;
		
		bufferHandle = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, bufferHandle);

		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
				GL_TEXTURE_2D, colorTex, 0);

		if (G) {
			normalTex = Utils.getTexture((FloatBuffer) null,
					RGL.getWidth(), RGL.getHeight(), GL_RGB16F, GL_REPEAT);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1,
					GL_TEXTURE_2D, normalTex, 0);
			
			positionTex = Utils.getTexture((FloatBuffer) null,
					RGL.getWidth(), RGL.getHeight(), GL_RGB16F, GL_REPEAT);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2,
					GL_TEXTURE_2D, positionTex, 0);
			
			// Notify
			int bufs[] = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1,
					GL_COLOR_ATTACHMENT2 };
			IntBuffer buf = BufferUtils.createIntBuffer(bufs.length);
			buf.put(bufs);
			buf.flip();
			glDrawBuffers(buf);
		}
		

		// Add depth buffer
		int depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, RGL.getWidth(),
				RGL.getHeight());
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthBuffer);

		// Check for completeness
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		switch (status) {
		case GL_FRAMEBUFFER_COMPLETE:
			RGL.log("Framebuffer is OK");
			break;
		default:
			RGL.log("Framebuffer failed");
			throw new RuntimeException("Framebuffer failed");
		}
	}

	public int getBufferHandle() {
		return bufferHandle;
	}
	
	public int getColorTex() {
		return colorTex;
	}

	public void setColorTex(int colorTex) {
		this.colorTex = colorTex;
	}

	public int getNormalTex() {
		return normalTex;
	}

	public void setNormalTex(int normalTex) {
		this.normalTex = normalTex;
	}

	public int getPositionTex() {
		return positionTex;
	}

	public void setPositionTex(int positionTex) {
		this.positionTex = positionTex;
	}
}
