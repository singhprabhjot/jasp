import org.neo4j.driver.v1.Record;

import java.util.List;

/**
 * Created by prabhjot.singh on 4/26/18.
 */
public class Query {
    DBConnector driver;

    public Query() {
        this.driver = DBConnector.getInstance();
    }

    public List<Record> runQuery() {
        List<Record> result=driver.runTx("MATCH (n:PERSON) RETURN {PERSON:n.username} LIMIT 10");
        return result;
    }


//    public void testQuery(){
//        TraversalDescription myTraversal = db.traversalDescription()
//                .depthFirst()
//                .expand(hipHopExpander)
//                .evaluator(hiphopEvaluator);
//
//        return  myTraversal.traverse(startNode).nodes().stream().distinct().map(BuiltInProcedures.NodeResult::new);

//    }
    public static void main(String[] args) {
        Query q = new Query();
        q.runQuery();
    }
}
