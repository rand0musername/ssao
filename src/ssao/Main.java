package ssao;

import rafgl.RGL;

public class Main {

	public static void main(String[] args) {
		// Init params
		RGL.setParami(RGL.IParam.WIDTH, 1280);
		RGL.setParami(RGL.IParam.HEIGHT, 720);
		RGL.setParami(RGL.IParam.FULLSCREEN, 0);
		RGL.setParami(RGL.IParam.MSAA, 4);
		RGL.setParami(RGL.IParam.VSYNC, 1);
		RGL.init();
		RGL.setTitle("SSAO");
		RGL.setRunning(true);

		// Init renderer
		Renderer renderer = new Renderer();

		// Print instructions
		StringBuilder builder = new StringBuilder();
		builder.append("*** Controls:\n");

		builder.append("* Camera:\n");
		builder.append("W/S - forward/back\n");
		builder.append("A/D - left/right\n");
		builder.append("Q/E - rotate left/right\n");
		builder.append("SPACE/CTRL - up/down\n");

		builder.append("* SSAO:\n");
		builder.append("T/G: inc/dec radius (default 0.6)\n");
		builder.append("Y/H: inc/dec bias (default 0.003)\n");
		builder.append("U/J: inc/dec power (default 0.8)\n");

		builder.append("* Debug:\n");
		builder.append("Left/right mouse click: next/prev mode\n");
		builder.append("Mode 0: SSAO on\n");
		builder.append("Mode 1: SSAO off\n");
		builder.append("Mode 2: Occlusion mask\n");
		builder.append("Mode 3: Smooth occlusion mask\n");

		System.out.println(builder.toString());

		// Main loop
		while (RGL.isRunning()) {
			RGL.handleEvents();
			renderer.handleEvents();
			renderer.drawFrame();
		}
		RGL.deinit();
	}
}
