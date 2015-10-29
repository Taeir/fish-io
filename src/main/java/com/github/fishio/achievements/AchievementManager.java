package com.github.fishio.achievements;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Class that holds all currently implemented achievements.
 */
public final class AchievementManager {
	public static final Achievement PLAYER_DEATH = new Achievement("playerDeath") {
		@Override
		public void updateAchievement(AchievementObserver observer) {
			if (!(observer instanceof PlayerDeathObserver)) {
				return;
			}
			
			int nr = ((PlayerDeathObserver) observer).getCounter();
			AchievementIO.addObserverCounter(PLAYER_DEATH.getName(), nr);

			if (nr >= 100) {
				setLevel(5);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 100 times");

			} else if (nr >= 50) {
				setLevel(4);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 50 times");

			} else if (nr >= 10) {
				setLevel(3);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 10 times");

			} else if (nr >= 5) {
				setLevel(2);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 5 times");

			} else if (nr >= 1) {
				setLevel(1);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for dying 1 time");
			} else {
				setLevel(0);
			}
		}
	};
	
	public static final Achievement ENEMY_KILL = new Achievement("enemyKill") {
		@Override
		public void updateAchievement(AchievementObserver observer) {
			if (!(observer instanceof EnemyKillObserver)) {
				return;
			}
			
			int nr = ((EnemyKillObserver) observer).getCounter();
			AchievementIO.addObserverCounter(ENEMY_KILL.getName(), nr);


			if (nr >= 500) {
				setLevel(5);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 500 times");
			} else if (nr >= 100) {
				setLevel(4);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 100 times");
			} else if (nr >= 50) {
				setLevel(3);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 50 times");
			} else if (nr >= 10) {
				setLevel(2);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 10 times");
			} else if (nr >= 3) {
				setLevel(1);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for killing an enemy fish 3 times");
			} else {
				setLevel(0);
			}
		}
	};
	
	public static final Achievement HIT_WALL = new Achievement("hitWall") {
		@Override
		public void updateAchievement(AchievementObserver observer) {
			if (!(observer instanceof HitWallObserver)) {
				return;
			}

			int nr = ((HitWallObserver) observer).getCounter();
			AchievementIO.addObserverCounter(HIT_WALL.getName(), nr);

			if (nr >= 500) {
				setLevel(5);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for hitting the outer wall 500 times");
			} else if (nr >= 100) {
				setLevel(4);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for hitting the outer wall 100 times");
			} else if (nr >= 50) {
				setLevel(3);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for hitting the outer wall 50 times");
			} else if (nr >= 10) {
				setLevel(2);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for hitting the outer wall 10 times");
			} else if (nr >= 1) {
				setLevel(1);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for hitting the outer wall once");
			} else {
				setLevel(0);
			}
		}
	};

	public static final Achievement LIVES_CONSUMPTION = new Achievement("livesConsumption") {
		@Override
		public void updateAchievement(AchievementObserver observer) {
			if (!(observer instanceof LivesConsumptionObserver)) {
				return;
			}

			int nr = ((LivesConsumptionObserver) observer).getCounter();
			AchievementIO.addObserverCounter(LIVES_CONSUMPTION.getName(), nr);

			if (nr >= 243) {
				setLevel(5);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for resurrecting 243 times");
			} else if (nr >= 81) {
				setLevel(4);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for resurrecting 81 times");
			} else if (nr >= 27) {
				setLevel(3);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for resurrecting 27 times");
			} else if (nr >= 9) {
				setLevel(2);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for resurrecting 9 times");
			} else if (nr >= 3) {
				setLevel(1);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for resurrecting 3 times");
			} else {
				setLevel(0);
			}
		}
	};

	public static final Achievement PLAYER_SCORE = new Achievement("playerScore") {
		@Override
		public void updateAchievement(AchievementObserver observer) {
			if (!(observer instanceof PlayerScoreObserver)) {
				return;
			}

			int nr = ((PlayerScoreObserver) observer).getCounter();
			AchievementIO.addObserverCounter(PLAYER_SCORE.getName(), nr);

			if (nr >= 1000) {
				setLevel(5);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for gaining a cumulative score of 1000");
			} else if (nr >= 500) {
				setLevel(4);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for gaining a cumulative score of 500");
			} else if (nr >= 200) {
				setLevel(3);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for gaining a cumulative score of 200");
			} else if (nr >= 50) {
				setLevel(2);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for gaining a cumulative score of 50");
			} else if (nr >= 10) {
				setLevel(1);
				Log.getLogger().log(LogLevel.INFO, "Achievement gained for gaining a cumulative score of 10");
			} else {
				setLevel(0);
			}
		}
	};

	private AchievementManager() { }
}
