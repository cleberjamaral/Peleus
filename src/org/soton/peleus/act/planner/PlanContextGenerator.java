package org.soton.peleus.act.planner;

import graphplan.Graphplan;
import graphplan.domain.Proposition;
import jason.asSyntax.Literal;
import jason.asSyntax.LogExpr;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;

import java.util.List;

import org.soton.peleus.act.planner.javagp.ProblemOperatorsImpl;

/**
 * A class responsible for deriving minimum context information for
 * new plans generated by the planner, and generalising the plan
 * for addition to the plan library.
 * @author meneguzz
 *
 */
public class PlanContextGenerator {
	private static PlanContextGenerator generator = null;
	
	public static PlanContextGenerator getInstance() {
		if(generator == null) {
			generator = new PlanContextGenerator();
		}
		return generator;
	}
	
	private PlanContextGenerator() {
		
	}
	
	/**
	 * Generates the context for the specified plan
	 * @param plan
	 * @return
	 */
	public LogicalFormula generateContext(List<String> plan, List<Plan> plans) {
		Graphplan graphplan = new Graphplan();
		ProblemOperatorsImpl operators = new ProblemOperatorsImpl(plans);
		List<Proposition> contextLiterals = graphplan.getPlanPreconditions(plan, operators.getDomainDescription());
		
		LogicalFormula context;
		
		if(contextLiterals.size() == 0) {
			context = Literal.LTrue;
		} else {
			context = (Literal) contextLiterals.get(0);
			for(int i=1; i < contextLiterals.size(); i++) {
				context = new LogExpr(context, LogExpr.LogicalOp.and, (Literal) contextLiterals.get(i));
			}
		}
		
		return context;
	}
}
