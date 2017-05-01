/*
 * Copyright (c) 2017. tangzx(love.tangzx@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tang.intellij.lua.debugger;

import com.intellij.debugger.impl.DebuggerUtilsEx;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.tang.intellij.lua.psi.LuaTypes;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by tangzx on 2017/5/1.
 */
public abstract class LuaDebuggerEvaluator extends XDebuggerEvaluator {
    @Nullable
    @Override
    public TextRange getExpressionRangeAtOffset(Project project, Document document, int offset, boolean sideEffectsAllowed) {
        final Ref<TextRange> currentRange = Ref.create(null);
        PsiDocumentManager.getInstance(project).commitAndRunReadAction(() -> {
            try {
                PsiElement element = DebuggerUtilsEx.findElementAt(PsiDocumentManager.getInstance(project).getPsiFile(document), offset);
                if (element == null || !element.isValid()) {
                    return;
                }
                IElementType type = element.getNode().getElementType();
                if (type == LuaTypes.ID) {
                    PsiElement parent = element.getParent();
                    if (parent instanceof PsiNamedElement) {
                        PsiElement id = parent;
                        if (parent instanceof PsiNameIdentifierOwner)
                            id = ((PsiNameIdentifierOwner) parent).getNameIdentifier();
                        if (id != null)
                            currentRange.set(id.getTextRange());
                    }
                }
            } catch (IndexNotReadyException ignored) {}
        });
        return currentRange.get();
    }
}
