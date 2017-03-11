package net.stinfoservices.helias.renaud.tempest.agent.friend.attaquant;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JFrame;

import net.stinfoservices.helias.renaud.tempest.IScreen;
import net.stinfoservices.helias.renaud.tempest.level.ILevel;
import net.stinfoservices.helias.renaud.tempest.level.ILevel2D;
import net.stinfoservices.helias.renaud.tempest.level.Level0;
import net.stinfoservices.helias.renaud.tempest.level.terrain.Killapede;

import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class CanonModelTest {

	private JFrame jFrame;

	@Test
	public void test() {
		openGLStart();
		ILevel2D level = new Level0(new IScreen() {
			@Override
			public int getScreenWidth() {
				return 480;
			}
			
			@Override
			public int getScreenHeight() {
				return 640;
			}

		});
		level.setArea(new Killapede());
		CanonModel canon = new CanonModel(level);
		canon.key(Keyboard.KEY_LEFT, true);
		canon.key(Keyboard.KEY_RIGHT, true);
		//canon.key(Keyboard.KEY_LEFT, true);
		assertEquals(0.0, canon.getProgression(),0); // 0
		canon.step();
		assertEquals(0.2, canon.getProgression(),0); // 1
		canon.step();
		assertEquals(0.4, canon.getProgression(),0); // 2
		canon.step();
		assertEquals(0.6, canon.getProgression(),0); // 3
		canon.step();
		assertEquals(0.8, canon.getProgression(),0); // 4
		canon.step();
		assertEquals(0.0, canon.getProgression(),0); // 5 = 0
		canon.step();
		assertEquals(0.2, canon.getProgression(),0);
		canon.step();
		assertEquals(0.4, canon.getProgression(),0);
	}

	private void openGLStart() {
		try {
			jFrame = new JFrame("Tempest game");
			// manque Display.destroy
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setLayout(new BorderLayout());
			Canvas parent = new Canvas();
			jFrame.add(parent, BorderLayout.CENTER);
			jFrame.setSize(480, 640);
			jFrame.setVisible(true);
//			jFrame.add("suck", BorderLayout.SOUTH);
			Display.setParent(parent);
//			Display.setVSyncEnabled(true);
			Display.setDisplayMode(new DisplayMode(480, 640));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// init OpenGL here
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 480, 0, 640, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glClearColor(0.0F,0.0F,0.0F,1.0F);
	}

}
