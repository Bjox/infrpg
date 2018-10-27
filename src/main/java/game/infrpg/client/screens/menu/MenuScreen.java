package game.infrpg.client.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.infrpg.client.InfrpgGame;
import game.infrpg.client.logic.AbstractScreen;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MenuScreen extends AbstractScreen
{

	private final ILogger logger;
	private final Stage stage;

	@Inject
	public MenuScreen(InfrpgGame game, ILogger logger)
	{
		super(game);
		this.logger = logger;
		this.logger.debug("Menu");

		this.stage = new Stage(new ScreenViewport());

		Skin mySkin = new Skin(Gdx.files.internal("skins/default/uiskin.json"));

		final int screenWidth = Gdx.graphics.getWidth();
		final int screenHeight = Gdx.graphics.getHeight();

		Label title = new Label("Infrpg", mySkin);
		title.setFontScale(5);
		title.setSize(screenWidth, 0);
		title.setPosition(0, screenHeight - 70);
		title.setAlignment(Align.center);
		stage.addActor(title);

		TextField ipField = new TextField("localhost", mySkin);
		ipField.setSize(200, 30);
		ipField.setPosition(screenWidth * 0.5f, screenHeight * 0.5f, Align.center);
		ipField.setAlignment(Align.center);
		stage.addActor(ipField);

		TextButton connectBtn = new TextButton("Connect", mySkin);
		connectBtn.setSize(120, 40);
		connectBtn.setPosition(screenWidth * 0.5f, ipField.getY() - ipField.getHeight(), Align.center);
		connectBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (connectBtn.isDisabled()) return;

				connectBtn.setText("Connecting...");
				connectBtn.setDisabled(true);

				InfrpgGame.execCommand("connect " + ipField.getText())
					.whenComplete((result, ex) -> {
						connectBtn.setText("Connect");
						connectBtn.setDisabled(false);
					});
			}
		});
		stage.addActor(connectBtn);

		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_NEAREST);
		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_NEAREST);

//		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
//		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
//		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
//		Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR_MIPMAP_LINEAR);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public int getRenderCalls()
	{
		return 0;
	}

}
