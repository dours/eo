/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2022 Yegor Bugayenko
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
package org.eolang.maven;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.yegor256.tojos.Tojo;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.TrBulk;
import com.yegor256.xsline.TrClasspath;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eolang.parser.ParsingTrain;

/**
 * Optimize XML files.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@Mojo(
    name = "optimize",
    defaultPhase = LifecyclePhase.PROCESS_SOURCES,
    threadSafe = true
)
public final class OptimizeMojo extends SafeMojo {

    /**
     * The directory where to place intermediary files.
     */
    public static final String STEPS = "02-steps";

    /**
     * The directory where to transpile to.
     */
    public static final String DIR = "03-optimize";

    @Override
    public void exec() throws IOException {
        final Collection<Tojo> sources = this.scopedTojos().select(
            row -> row.exists(AssembleMojo.ATTR_XMIR)
        );
        int done = 0;
        for (final Tojo tojo : sources) {
            final Path src = Paths.get(tojo.get(AssembleMojo.ATTR_XMIR));
            if (tojo.exists(AssembleMojo.ATTR_XMIR2)) {
                final Path tgt = Paths.get(tojo.get(AssembleMojo.ATTR_XMIR2));
                if (tgt.toFile().lastModified() >= src.toFile().lastModified()) {
                    Logger.debug(
                        this, "Already optimized %s to %s",
                        Save.rel(src), Save.rel(tgt)
                    );
                    continue;
                }
            }
            ++done;
            tojo.set(
                AssembleMojo.ATTR_XMIR2,
                this.optimize(src).toAbsolutePath().toString()
            );
        }
        if (done > 0) {
            Logger.info(this, "Optimized %d out of %d XMIR program(s)", done, sources.size());
        } else {
            Logger.debug(this, "No XMIR programs out of %d optimized", sources.size());
        }
    }

    /**
     * Optimize XML file after parsing.
     *
     * @param file EO file
     * @return The file with optimized XMIR
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    private Path optimize(final Path file) throws IOException {
        final String name = new XMLDocument(file).xpath("/program/@name").get(0);
        final Place place = new Place(name);
        final Path dir = place.make(
            this.targetDir.toPath().resolve(OptimizeMojo.STEPS), ""
        );
        final Path target = place.make(
            this.targetDir.toPath().resolve(OptimizeMojo.DIR), Transpiler.EXT
        );
        Train<Shift> train = new TrBulk<>(new TrClasspath<>(new ParsingTrain())).with(
            Arrays.asList(
                "/org/eolang/parser/optimize/globals-to-abstracts.xsl",
                "/org/eolang/parser/optimize/remove-refs.xsl",
                "/org/eolang/parser/optimize/abstracts-float-up.xsl",
                "/org/eolang/parser/optimize/remove-levels.xsl",
                "/org/eolang/parser/add-refs.xsl",
                "/org/eolang/parser/optimize/fix-missed-names.xsl",
                "/org/eolang/parser/add-refs.xsl",
                "/org/eolang/parser/errors/broken-refs.xsl"
            )
        ).back().back();
        train = new SpyTrain(train, dir);
        final XML out = new Xsline(train).pass(new XMLDocument(file));
        new Save(out.toString(), target).save();
        Logger.debug(
            this, "Optimized %s (program:%s) to %s, all steps are in %s",
            Save.rel(file), name, Save.rel(target), Save.rel(dir)
        );
        return target;
    }

}
