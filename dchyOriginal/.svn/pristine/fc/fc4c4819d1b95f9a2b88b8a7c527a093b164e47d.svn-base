/*
 * Project:  onemap
 * Module:   server
 * File:     FilterToSolrQuery.java
 * Modifier: xyang
 * Modified: 2013-05-23 03:21:27
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.data;

import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.opengis.filter.*;
import org.opengis.filter.expression.*;
import org.opengis.filter.spatial.*;
import org.opengis.filter.temporal.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-18
 */
class FilterToSolrQuery implements FilterVisitor, ExpressionVisitor {
    protected Writer out;

    public FilterToSolrQuery(Writer out) {
        this.out = out;
    }

    public FilterToSolrQuery() {
        out = new StringWriter();
    }

    public void setOut(Writer out) {
        this.out = out;
    }

    private void write(String s) {
        try {
            out.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        write(" ");
        return extraData;
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return visit(expression, "sum", extraData);
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return visit(expression, "div", extraData);
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        write(expression.getName());
        write("(");
        List<Expression> parameters = expression.getParameters();
        for (int i = 0, len = parameters.size(); i < len; i++) {
            Expression e = parameters.get(i);
            e.accept(this, extraData);
            if (i < len - 1) {
                write(",");
            }
        }
        write(")");
        return extraData;
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        write(expression.getValue().toString());
        return extraData;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return visit(expression, "product", extraData);
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        write(expression.getPropertyName());
        return extraData;
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("Subtract expression");
    }

    protected Object visit(BinaryExpression expression, String operator, Object extraData) throws RuntimeException {
        write(operator);
        write("(");
        expression.getExpression1().accept(this, extraData);
        write(",");
        expression.getExpression2().accept(this, extraData);
        write(")");
        return extraData;
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        write("id:0");
        return extraData;
    }

    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return extraData;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return visit(filter, "AND", extraData);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        write("id:(");
        Iterator it = filter.getIDs().iterator();
        boolean isFirst = true;
        while (it.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                write(" OR ");
            }
            write(it.next().toString());
        }
        write(")");
        return extraData;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        write("-");
        filter.getFilter().accept(this, extraData);
        return extraData;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return visit(filter, "OR", extraData);
    }

    protected Object visit(BinaryLogicOperator filter, String type, Object extraData) {
        write("(");
        List<Filter> children = filter.getChildren();
        for (int i = 0, len = children.size(); i < len; i++) {
            Filter f = children.get(i);
            f.accept(this, extraData);
            if (i < len - 1) {
                write(" " + type + " ");
            }
        }
        write(")");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();
        expr.accept(this, extraData);
        write(":[");
        lowerbounds.accept(this, extraData);
        write(" TO ");
        upperbounds.accept(this, extraData);
        write("]");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":");
        filter.getExpression2().accept(this, extraData);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        write("-");
        filter.getExpression1().accept(this, extraData);
        write(":");
        filter.getExpression2().accept(this, extraData);
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":{");
        filter.getExpression2().accept(this, extraData);
        write(" TO *}");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":[");
        filter.getExpression2().accept(this, extraData);
        write(" TO *]");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":{* TO ");
        filter.getExpression2().accept(this, extraData);
        write("}");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":[* TO ");
        filter.getExpression2().accept(this, extraData);
        write("]");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        filter.getExpression().accept(this, extraData);
        write(":");
        write(StringUtils.replace(filter.getLiteral(), "%", "*"));
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        write("-");
        filter.getExpression().accept(this, extraData);
        write(":[* To *]");
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return visit(filter, "BBoxIntersects", extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return visit(filter, "Contains", extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return visit(filter, "IsDisjointTo", extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return visit(filter, "IsWithin", extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return visit(filter, "Intersects", extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return visit(filter, "Overlaps", extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return visit(filter, "IsWithin", extraData);
    }

    @Override
    public Object visit(After after, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Before before, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(During during, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException();
    }

    private Object visit(BinarySpatialOperator filter, String operation, Object extraData) {
        filter.getExpression1().accept(this, extraData);
        write(":\"" + operation + "(");
        write(((Geometry) ((Literal) filter.getExpression2()).getValue()).toText());
        write(")\"");
        return extraData;
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
