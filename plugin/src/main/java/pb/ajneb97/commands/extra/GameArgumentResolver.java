package pb.ajneb97.commands.extra;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class GameArgumentResolver extends ArgumentResolver<CommandSender, Game> {

    @Inject
    private GameManager gameManager;
    @Inject
    private MessageHandler messageHandler;

    @Override
    protected ParseResult<Game> parse(
            Invocation<CommandSender> invocation,
            Argument<Game> argument,
            String string
    ) {
        if (!gameManager.gameExists(string)) {
            messageHandler.sendMessage(invocation.sender(), Messages.ARENA_DOES_NOT_EXIST);
            return ParseResult.failure(Messages.ARENA_DOES_NOT_EXIST);
        }

        return ParseResult.success(gameManager.getGame(string));
    }

    @Override
    public SuggestionResult suggest(
            Invocation<CommandSender> invocation,
            Argument<Game> argument,
            SuggestionContext context
    ) {
        return gameManager.getGames().stream().map(Game::getName).collect(SuggestionResult.collector());
    }
}
