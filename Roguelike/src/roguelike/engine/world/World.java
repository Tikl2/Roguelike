package roguelike.engine.world;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roguelike.engine.asset.AssetManager;
import roguelike.engine.entity.Entity;
import roguelike.engine.entity.EntityConstants;
import roguelike.engine.entity.EntityImageFactory;
import roguelike.engine.entity.PlayerCharacter;
import roguelike.engine.input.Controller;
import roguelike.engine.world.tile.TileConstants;
import roguelike.exceptions.AssetInitializationException;
import roguelike.exceptions.UninitializedAssetManagerException;

/**
 * World exists as a manager of the map and entities. It manages entities and
 * ensures that they do not exhibit impossible behaviors such as leaving the map
 * or having negative health.
 */
public class World
{
	private Map map;
	private Entity player;
	private List<Entity> otherEntities;

	String entityFilePath = "assets/graphics/entities/entity.txt";
	private AssetManager<Integer, Image> entityAssets;

	Controller controller;

	// int currentId;

	public World() throws UninitializedAssetManagerException, IOException,
			AssetInitializationException
	{
		entityAssets = new AssetManager<Integer, Image>(
				new EntityImageFactory(), entityFilePath);
		map = new Map();
		player = new PlayerCharacter(EntityConstants.PLAYER_CHARACTER,
				new Point(map.getHeight() / 2, map.getWidth() / 2),
				entityAssets.getAsset(EntityConstants.PLAYER_CHARACTER));
		otherEntities = new ArrayList<Entity>();
		controller = new Controller(this);
	}

	public Map getMap()
	{
		return map;
	}

	public Controller getController()
	{
		return controller;
	}

	public Entity getPlayer()
	{
		return player;
	}

	public void processKeyEvent(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
				|| keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT)
		{
			Point playerPosition = player.getPosition();

			int xDisplace = 0;
			int yDisplace = 0;
			switch (keyCode)
			{
				case KeyEvent.VK_UP:
					yDisplace = -1;
					break;
				case KeyEvent.VK_DOWN:
					yDisplace = 1;
					break;
				case KeyEvent.VK_LEFT:
					xDisplace = -1;
					break;
				case KeyEvent.VK_RIGHT:
					xDisplace = 1;
					break;
			}

			Point newPosition = MapUtilities.displacePoint(playerPosition,
					xDisplace, yDisplace);
			if (newPosition.x < 0 || newPosition.x > map.getWidth() - 1
					|| newPosition.y < 0 || newPosition.y > map.getHeight() - 1)
			{
				return;
			}

			if (map.get(newPosition).getTileType() != TileConstants.NON_CLIPPABLE)
			{
				player.setPosition(newPosition);
			}
		}
	}
}
