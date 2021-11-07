package EOorg.EOeolang;

import org.eolang.*;

public class EOdebugMagic$EOseq extends PhDefault {
    public EOdebugMagic$EOseq(final Phi sigma) {
        super(sigma);
        this.add("a", new AtFree());
        this.add("b", new AtFree());
        this.add("φ", new AtOnce(new AtComposite(this, self -> {
            new Dataized(self.attr("a").get()).take();
            return new PhMethod(self, "b");
        })));
    }

}
