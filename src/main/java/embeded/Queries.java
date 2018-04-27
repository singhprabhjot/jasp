package embeded;

import faker.CreateFakePersons;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.*;
import utilities.DBConnector;

/**
 * Created by jaspreet.kaur on 4/26/18.
 * Help taken from informit.com/
 */
public class Queries {
    GraphDatabaseService graphDB;

    public Queries() {
        graphDB = DBConnector.getInstance().getGraphDBConnector();
    }

    // Find all gotCharacters in our database
    public void runQueryForGOTCharacters() {
        System.out.println("\n\nQUERY: Find all gotCharacters in our database\n");
        ResourceIterator<Node> gotCharacters = graphDB.findNodes(LoadData.Labels.GOT_CHARACTERS);
        System.out.println("GOT Characters: ----->>>>");
        while (gotCharacters.hasNext()) {
            Node gotCharacter = gotCharacters.next();
            System.out.println("    " + gotCharacter.getProperty("name"));
        }
    }

    // Find all users
    public void runQueryToFindUsers() {
        System.out.println("\n\nQUERY: Find all users");
        System.out.println("Users ------>>>>");
        int i = 0;
        ResourceIterator<Node> users = graphDB.findNodes(LoadData.Labels.USER);
        while (users.hasNext()) {
            Node user = users.next();
            try {
                if (i % 50 == 0) {
                    System.out.println();
                }
                System.out.print("    " + user.getProperty("username"));
            } catch (Exception e) {

            }
            i++;
        }
    }


    public void getAverageRating() {
        System.out.println("\n\nQUERY: Get Average GOT Character Ratings");
        ResourceIterator<Node> gotCharacters = graphDB.findNodes(LoadData.Labels.GOT_CHARACTERS);
        System.out.println("GOT Character Ratings:");
        while (gotCharacters.hasNext()) {
            Node gotChar = gotCharacters.next();

            // Follow all HAS_SEEN relationships and get their star rating
            Iterable<Relationship> relationships = gotChar.getRelationships(
                    Direction.INCOMING, LoadData.RelationshipTypes.LIKES_CHARACTER);
            int totalStars = 0;
            int relationshipCount = 0;
            for (Relationship relationship : relationships) {
                Integer stars = (Integer) relationship.getProperty("stars");
                totalStars += stars;
                relationshipCount++;
            }
            System.out.println("\t" + gotChar.getProperty("name") + ", Viewers: " +
                    relationshipCount + ", Average rating: " +
                    (float) totalStars / relationshipCount);
        }
    }

    //Next let's find all characters that a user has seen:
    public void getCharactersAUserHasLiked() {
        System.out.println("\n\nQUERY: Find all characters that a user has liked");
        ResourceIterator<Node> users = graphDB.findNodes(LoadData.Labels.USER);
        System.out.println("Users:");
        while (users.hasNext()) {
            Node user = users.next();
            System.out.print("\t" + user.getProperty("username") + " has liked ");
            for (Relationship relationship : user.getRelationships(
                    LoadData.RelationshipTypes.LIKES_CHARACTER)) {
                Node gotCharacter = relationship.getOtherNode(user);
                System.out.print("\t" + gotCharacter.getProperty("name"));
            }
            System.out.println();
        }

    }

    public void gotCharactersThatMyFriendsHaveLiked() {
        for (int i = 0; i < 5; i++) {
            long startTime=System.currentTimeMillis();
            String username = CreateFakePersons.personsList.get(i).getUsername();
            Node person = findAPerson(username);

            // Find usernames's friends
            TraversalDescription myFriends = graphDB.traversalDescription()
                    .breadthFirst()
                    .relationships(LoadData.RelationshipTypes.IS_FRIEND)
                    .evaluator(Evaluators.atDepth(10));
            Traverser traverser = myFriends.traverse(person);
            System.out.println(username + "'s friends: ");
            for (Node friend : traverser.nodes()) {
                System.out.println("\t" + friend.getProperty("username"));
            }


            //
            TraversalDescription gOTCharactersThatFriendsLike = graphDB.traversalDescription()
                    // Choose a depth-first search strategy
                    .depthFirst()

                    // At depth 0 traverse the IS_FRIEND relationships,
                    // at a depth of 1 traverse the LIKES_CHARACTER relationship
                    .expand(new PathExpander<Object>() {
                        @Override
                        public Iterable<Relationship> expand(Path path,
                                                             BranchState<Object> objectBranchState) {
                            // Get the depth of this node
                            int depth = path.length();

                            if (depth == 0) {
                                // Depth of 0 means the user's node (starting node)
                                return path.endNode().getRelationships(
                                        LoadData.RelationshipTypes.IS_FRIEND);
                            } else {
                                // A depth of 1 would mean that we're at a friend and
                                // should expand his LIKES_CHARACTER relationships
                                return path.endNode().getRelationships(
                                        LoadData.RelationshipTypes.LIKES_CHARACTER);
                            }
                        }

                        @Override
                        public PathExpander<Object> reverse() {
                            return null;
                        }
                    })

                    // Only go down to a depth of 10
                    .evaluator(Evaluators.atDepth(10))

                    // Only return gOTCharacters
                    .evaluator(new Evaluator() {
                        @Override
                        public Evaluation evaluate(Path path) {
                            if (path.endNode().hasLabel(LoadData.Labels.GOT_CHARACTERS)) {
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            return Evaluation.EXCLUDE_AND_CONTINUE;
                        }
                    });

            traverser = gOTCharactersThatFriendsLike.traverse(person);

            System.out.println("GotCharacters that " + username + " friends have liked: ");
            for (Node gotCharacter : traverser.nodes()) {
                System.out.println("\t" + gotCharacter.getProperty("name"));
            }
        }
    }

    private Node findAPerson(String username) {
        Node myNode = graphDB.findNode(LoadData.Labels.USER, "username", username);
        return myNode;
    }
}
