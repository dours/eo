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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eolang.*;

/**
 * REGEX.
 *
 * @since 0.2
 */
public class EOregex$EOmatch extends PhDefault {

    /**
     * Text attribute.
     */
    public static final String TXT = "txt";

    public EOregex$EOmatch(final Phi sigma) {
        super(sigma);
        this.add(EOregex$EOmatch.TXT, new AtFree());
        this.add("φ", new AtOnce(new AtComposite(this, self -> {
            final Pattern pattern = new Dataized(
                self.attr("ρ").get()
            ).take(Pattern.class);
            final String txt = new Dataized(
                self.attr(EOregex$EOmatch.TXT).get()
            ).take(String.class);
            final Matcher matcher = pattern.matcher(txt);
            if (matcher.matches()) {
                final Phi[] dest = new Phi[matcher.groupCount()];
                return new Data.ToPhi(dest);
            } else {
                return new Data.ToPhi(new Phi[]{});
            }
        })));
    }

}
