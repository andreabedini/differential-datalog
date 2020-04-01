/*
 * Copyright (c) 2019 VMware Inc. All Rights Reserved.
 * SPDX-License-Identifier: MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice (including the next paragraph) shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmware.ddlog.ir;

import javax.annotation.Nullable;

public class DDlogEITE extends DDlogExpression {
    private final DDlogExpression cond;
    private final DDlogExpression then;
    @Nullable
    private final DDlogExpression eelse;

    public DDlogEITE(DDlogExpression cond, DDlogExpression then, @Nullable DDlogExpression eelse) {
        super(eelse == null ? then.getType() :
                (then.getType().mayBeNull ? eelse.getType() : then.getType()));
        if (eelse != null)
            DDlogType.checkCompatible(then.getType(), eelse.getType(), true);
        this.cond = this.checkNull(cond);
        this.then = this.checkNull(then);
        this.eelse = eelse;
        if (!(this.cond.type instanceof DDlogTBool))
            throw new RuntimeException("Condition is not Boolean");
    }

    @Override
    public String toString() {
        String result =  "if " + this.cond.toString() + " {\n" +
                this.then.toString() + "}";
        if (this.eelse != null)
            result += " else {\n" +
                this.eelse.toString() + "}";
        return result;
    }
}
