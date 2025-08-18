import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.conditions.impl.placeholder_condition.PlaceholderCondition;
import me.ogali.xenithlibrary.shared.Context;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlaceholderConditionTest {

    @Mock
    private Evaluator evaluator;

    @Mock
    private Player player;

    @Mock
    private Context context;

    private PlaceholderCondition playerNameCondition;
    private final String playerNamePlaceholder = "%player_name%";
    private final String playerName = "xxAli";


    private final PlaceholderCondition playerLevelCondition = new PlaceholderCondition(Evaluator.GREATER_OR_EQUAL, "%player_level%", 15);
    private final String playerLevelPlaceholder = "%player_level%";
    private final String playerLevel = "12";

    @Before
    public void setUp() {
        playerNameCondition = new PlaceholderCondition(Evaluator.EQUAL, playerNamePlaceholder, playerName);

        when(context.getPlayer()).thenReturn(player);

        // Set XenithLibrary.PAPI to true for testing
        XenithLibrary.PAPI = true;
    }

    @Test
    public void testConditionWithValidContext() {
        try (MockedStatic<PlaceholderAPI> placeholderAPIMocked = mockStatic(PlaceholderAPI.class)) {
            placeholderAPIMocked.when(() -> PlaceholderAPI.setPlaceholders(player, playerNamePlaceholder))
                    .thenReturn(playerName);

            assertTrue(playerNameCondition.test(context));
        }
    }

    @Test
    public void testConditionWithValidContext2() {
        try (MockedStatic<PlaceholderAPI> placeholderAPIMocked = mockStatic(PlaceholderAPI.class)) {
            placeholderAPIMocked.when(() -> PlaceholderAPI.setPlaceholders(player, playerLevelPlaceholder))
                    .thenReturn(playerLevel);

            assertFalse(playerLevelCondition.test(context));
        }
    }

    @Test
    public void testConditionWhenPapiDisabled() {
        XenithLibrary.PAPI = false;

        boolean result = playerNameCondition.test(context);

        verify(evaluator, never()).evaluate(anyString(), anyString());
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConditionWithNullPlayer() {
        when(context.getPlayer()).thenReturn(null);

        playerNameCondition.test(context);
    }
}