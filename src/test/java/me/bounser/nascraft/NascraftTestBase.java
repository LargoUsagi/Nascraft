package me.bounser.nascraft;


import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import net.milkbowl.vault.economy.Economy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public abstract class NascraftTestBase {


    protected ServerMock server;
    protected  Nascraft nascraft;

    protected MockPlugin vault;
    protected Economy economy;

    protected MockPlugin advancedGUI;




    @BeforeEach
    protected void setUp() {
        server = MockBukkit.mock();
        vault = MockBukkit.createMockPlugin("Vault");
        MockBukkit.createMockPlugin("ProtocolLib");
        advancedGUI = MockBukkit.createMockPlugin("AdvancedGUI");
        advancedGUI.saveDefaultConfig();
        File layoutFolder = new File(advancedGUI.getDataFolder().getParent() + "/AdvancedGUI/layout");
        layoutFolder.mkdirs();
        nascraft = MockBukkit.load(Nascraft.class);
        economy = mock(Economy.class);

    }

    @AfterEach
    protected void tearDown() {
        MockBukkit.unmock();
    }


    @Test
    void verifyIsInTestMode() {
        assertTrue(nascraft.isUnitTest());
    }

}