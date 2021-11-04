package EOorg.EOeolang;

import org.eolang.*;

public final class EOdebugMagic extends PhDefault {

    public EOdebugMagic(final Phi sigma) {
      super(sigma);
        this.add("print", new AtOnce(new AtComposite(this, EOdebugMagic$EOprint::new)));
        this.add("printDataized", new AtOnce(new AtComposite(this, EOdebugMagic$EOprintDataized::new)));
    }


}
