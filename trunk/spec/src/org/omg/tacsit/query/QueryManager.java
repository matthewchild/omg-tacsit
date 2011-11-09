/**
 * @(#) QueryManager.java
 */

package org.omg.tacsit.query;

import java.util.Collection;
import org.omg.tacsit.controller.Entity;

/**
 * The QueryManager supports the execution of queries for entities against the
 * TacSit Controller.
 * 
 * Queries submitted through the QueryManager are transient in nature; that is,
 * the results of a query represent the state of the TacSit Controller at the
 * time the Query was executed. Query results will not be maintained by the
 * QueryManager as the state of the TacSit Controller changes. Since the
 * QueryManager treats the Query objects as stateless, the same Query may be
 * re-used for any number of subsequent submissions.
 */
public interface QueryManager
{
	/**
	 * Returns the set of all Entities that satisfy the criteria expressed in
	 * the given EntityQuery.
	 * 
	 * @return 
	 */
	Collection<Entity> submitEntityQuery( EntityQuery query );
	
	
}
