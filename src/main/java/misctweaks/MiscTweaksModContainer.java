package misctweaks;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

public class MiscTweaksModContainer extends DummyModContainer
{
    public MiscTweaksModContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "misctweaks";
        meta.name = "MiscTweaks";
        meta.description = "Tweaks some things using asm. For now it quarters the speed boost given by techguns armor.";
        meta.version = "1.7.10-1.0";
        meta.authorList = Arrays.asList("build2stone");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}