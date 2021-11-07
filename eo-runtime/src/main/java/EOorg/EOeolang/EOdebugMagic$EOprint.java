package EOorg.EOeolang;

import org.eolang.*;

public final class EOdebugMagic$EOprint extends PhDefault {
    private static final String PREFIX = "prefix";
    private static final String OBJ = "obj";

    public EOdebugMagic$EOprint(final Phi sigma) {
        super(sigma);
        this.add(PREFIX, new AtFree(/* default */));
        this.add(OBJ, new AtFree(/* default */));
        this.add("φ", new AtOnce(new AtComposite(this, rho -> {
            final String prefix = new Dataized(
                    rho.attr(PREFIX).get()
            ).take(String.class);
            System.err.println(prefix + rho.attr(OBJ).get().toString() + " at " + java.time.LocalTime.now());
            return new PhMethod(rho, "obj");
        })));
    }
}
