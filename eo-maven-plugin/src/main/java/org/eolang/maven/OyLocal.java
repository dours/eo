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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import org.cactoos.Input;
import org.cactoos.io.InputOf;

/**
 * Objectionary stored locally.
 *
 * @since 1.0
 */
public final class OyLocal implements Objectionary {
    /**
     * Local storage.
     */
    private final Path eopath;

    /**
     * Version.
     */
    private final String version;

    /**
     * Ctor.
     * @param ver Version.
     * @param path Root.
     */
    public OyLocal(final String ver, final Path path) {
        this.version = ver;
        this.eopath = path;
    }

    @Override
    public Input get(final String name) throws IOException {
        final Path file = new Place(name).make(
            this.eopath
                .resolve("sources")
                .resolve(this.version),
            "eo"
        );
        if (!file.toFile().exists()) {
            throw new FileNotFoundException(name);
        }
        return new InputOf(file);
    }
}
