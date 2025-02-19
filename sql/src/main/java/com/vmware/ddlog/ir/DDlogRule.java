/*
 * Copyright (c) 2021 VMware, Inc.
 * SPDX-License-Identifier: MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.vmware.ddlog.ir;

import com.facebook.presto.sql.tree.Node;
import com.vmware.ddlog.util.Linq;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Haskell rule allows multiple values on the LHS,
 * but when translating SQL we never need more than 1.
 */
public class DDlogRule extends DDlogNode {
    public final DDlogAtom head;
    public final List<RuleBodyTerm> body;

    public DDlogRule(@Nullable Node node, DDlogAtom head, List<RuleBodyTerm> body) {
        super(node);
        this.head = this.checkNull(head);
        this.body = this.checkNull(body);
    }

    public DDlogRule(@Nullable Node node, DDlogAtom head, RuleBodyTerm body) {
        this(node, head, new ArrayList<RuleBodyTerm>());
        this.body.add(this.checkNull(body));
    }

    @Override
    public String toString() {
        String result = "";
        /*
        result += this.comments();
        if (!result.isEmpty())
            result += "\n";
         */
        result += this.head.toString();
        if (!this.body.isEmpty()) {
            result += " :- " + String.join(",",
                Linq.map(this.body, RuleBodyTerm::toString));
        }
        result += ".";
        return result;
    }
}
