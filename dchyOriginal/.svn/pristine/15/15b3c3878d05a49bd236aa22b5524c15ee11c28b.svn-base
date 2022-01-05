/*
 * Project:  onemap
 * Module:   server
 * File:     IKTokenizerFactory.java
 * Modifier: xyang
 * Modified: 2013-05-17 05:19:06
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;
import org.wltea.analyzer.lucene.IKTokenizer;

import java.io.Reader;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-2-6
 */
public final class IKTokenizerFactory extends TokenizerFactory {
    private boolean useSmart = false;

    public IKTokenizerFactory(Map<String, String> args) {
        super(args);
        useSmart = Boolean.parseBoolean(args.get("useSmart"));
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public boolean isUseSmart() {
        return useSmart;
    }

    @Override
    public Tokenizer create(AttributeSource.AttributeFactory factory, Reader input) {
        return new IKTokenizer(input, isUseSmart());
    }
}
