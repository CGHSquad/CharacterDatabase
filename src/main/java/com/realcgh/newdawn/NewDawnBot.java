package com.realcgh.newdawn;

import com.realcgh.newdawn.commands.*;
import com.realcgh.newdawn.database.SQLiteSource;
import com.realcgh.newdawn.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * This is the main class where we initialize our bot.
 *
 * @author TechnoVision <a href="https://www.youtube.com/TechnoVisionTV">...</a>
 */

public class NewDawnBot {
    private static final Logger logger = LoggerFactory.getLogger(NewDawnBot.class);
    private final Dotenv config; //Variable to load in private ENV files
    private final ShardManager shardManager;

    /**
     * Loads environment variables and builds the bot shard manager.
     * @throws InvalidTokenException occurs when bot token is invalid.
     */

    public NewDawnBot() throws InvalidTokenException, SQLException {
        SQLiteSource.getConnection();

        config = Dotenv.configure().load();//loading in the info from the ENV file
        String token = config.get("TOKEN"); //Getting the Token Variable from ENV to initialize to string

        // Initialize the Database

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);//Allows bot to run on many instances
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Team Sigma Character Rankings After Timeskip (Weakest to Strongest)"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES);
        builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);//lazy loading
        builder.setChunkingFilter(ChunkingFilter.ALL);//Forces bot to cache all users on start-up Warning: Only use this with MemberCachePolicy, or it'll mess up your bot
        builder.enableCache(CacheFlag.ONLINE_STATUS);//used to enable certain cache for things such as activity or roles
        shardManager = builder.build();

        //Register Listeners
        CommandManager manager = new CommandManager();
        shardManager.addEventListener(new EventListener(), new CommandManager(), manager);
        manager.add(new Embed());
        manager.add(new userRoles());
        manager.add(new roles());
        manager.add(new welcome());
        manager.add(new createCharacter());

        logger.info("Bot initialized successfully");
    }

    public Dotenv getConfig(){
        return config;
    }

    /**
     * Retrieves the bot shard manager.
     * @return the ShardManager instance for the bot.
     */

    public ShardManager getShardManager(){return shardManager;}

    /**
     * Main method where we start our bot.
     */

    public static void main(String[] args) {
        try {
            NewDawnBot bot = new NewDawnBot();
        }catch (InvalidTokenException e){
            System.out.println("ERROR: Provided bot token is invalid!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
