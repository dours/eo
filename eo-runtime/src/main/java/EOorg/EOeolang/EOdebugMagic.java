package EOorg.EOeolang;

import org.eolang.*;

public final class EOdebugMagic extends PhDefault {

    private static final String PREFIX = "prefix";
    private static final String OBJ = "obj";

    public EOdebugMagic(final Phi sigma) {
      super(sigma);
        this.add("print", new AtOnce(new AtComposite(this, EOdebugMagic$EOprint::new)));
        this.add("printDataized", new AtOnce(new AtComposite(this, EOdebugMagic$EOprintDataized::new)));
    }

    public final class EOdebugMagic$EOprint extends PhDefault {
      public EOdebugMagic$EOprint(final Phi sigma) {
        super(sigma);
        this.add(PREFIX, new AtFree(/* default */));
        this.add(OBJ, new AtFree(/* default */));
        this.add("φ", new AtOnce(new AtComposite(this, rho -> {
          final String prefix = new Dataized(
                  rho.attr(PREFIX).get()
          ).take(String.class);
          System.out.println(prefix + rho.attr(OBJ).get().toString());
          return new PhMethod(rho, "obj");
        })));
      }
    }

    public final class EOdebugMagic$EOprintDataized extends PhDefault {
        public EOdebugMagic$EOprintDataized(final Phi sigma) {
            super(sigma);
            this.add(PREFIX, new AtFree(/* default */));
            this.add(OBJ, new AtFree(/* default */));
            this.add("φ", new AtOnce(new AtComposite(this, rho -> {
                final String prefix = new Dataized(
                        rho.attr(PREFIX).get()
                ).take(String.class);
                System.out.println(prefix +
                        (new Dataized(rho.attr(OBJ).get()).take()).toString());
                return new PhMethod(rho, "obj");
            })));
        }
    }

    public class EOdebugMagic$EOseq extends PhDefault {

        /**
         * Attribute name.
         */
        private static final String NAME = "steps";

        public EOdebugMagic$EOseq(final Phi sigma) {
            super(sigma);
            this.add(NAME, new AtVararg());
            this.add("φ", new AtComposite(this, self -> {
                final Phi[] args = new Dataized(
                        self.attr(NAME).get()
                ).take(Phi[].class);
                Object result = false;
                for (final Phi arg : args) {
                    result = new Dataized(arg).take();
                }
                return new Data.ToPhi(result);
            }));
        }

    }


}
