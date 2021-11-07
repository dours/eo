/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package EOorg.EOeolang.EOtxt;

import java.util.Collection;
import java.util.LinkedList;

import org.eolang.*;

/**
 * Sprintf.
 *
 * @since 0.2
 */
public class EOsprintf extends PhDefault {
    /**
     * Format attr name.
     */
    private static final String FMT = "format";

    /**
     * Arguments attr name.
     */
    private static final String AGV = "args";

    public EOsprintf(final Phi sigma) {
        super(sigma);
        this.add(EOsprintf.FMT, new AtFree());
        this.add(EOsprintf.AGV, new AtVararg());
        this.add("φ", new AtOnce(new AtComposite(this, rho -> {
            final String format = new Dataized(
                rho.attr(EOsprintf.FMT).get()
            ).take(String.class);
            final Phi[] args = new Dataized(
                rho.attr(EOsprintf.AGV).get()
            ).take(Phi[].class);
            final Collection<Object> items = new LinkedList<>();
            for (final Phi arg : args) {
                items.add(new Dataized(arg).take());
            }
            return new Data.ToPhi(String.format(format, items.toArray()));
        })));
    }

}
