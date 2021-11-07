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

package EOorg.EOeolang;

import org.eolang.*;

/**
 * SEQ.
 *
 * @since 1.0
 */
public class EOseq extends PhDefault {

    /**
     * Attribute name.
     */
    private static final String NAME = "steps";

    public EOseq(final Phi sigma) {
        super(sigma);
        this.add(EOseq.NAME, new AtVararg());
        this.add("φ", new AtOnce(new AtComposite(this, self -> {
            final Phi[] args = new Dataized(
                self.attr(EOseq.NAME).get()
            ).take(Phi[].class);
            Object result = false;
            for (final Phi arg : args) {
                result = new Dataized(arg).take();
            }
            return new Data.ToPhi(result);
        })));
    }

}
