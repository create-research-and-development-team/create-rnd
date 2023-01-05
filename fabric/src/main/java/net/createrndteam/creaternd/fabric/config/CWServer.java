package net.createrndteam.creaternd.fabric.config;

public class CWServer extends CWConfigBase {


    public final CWKinetics kinetics = nested(0, CWKinetics::new, Comments.kinetics);

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String kinetics = "Parameters and abilities of VS CreateRND's kinetic mechanisms";
    }

}
