% Non-Optimal path solution returned in the argument in the form of a list.
findRoute(L) :- 
        listify(UL), % Creates an unsorted list of pools.
        msort(UL,SL), % Sort the created list from left to write.
        growTree(SL, RootNode, SLwoRoot), % Get a tree structure with just the root
        growTree(SLwoRoot, FirstChild, SLwoFirstChild), % Get a tree structure with just the first child
        addChild(RootNode, FirstChild, RNwFChild),
        assert(RNwFChild), % Record RootNode
        assert(FirstChild), % Record First Child
        nodeify(SLwoFirstChild),
        findall(node(pool( P, Longitude, Latitudes), D, C), node(pool( P, Longitude, Latitudes), D, C), NodeList).
        addToClosest(NodeList), % Find the closest nodes to the children and add.



% Load the unsorted database
:- [database].

% Dynamic predicates
:- dynamic tree/3
:- dynamic node/3

% Find the west most pool
findWestMost(pool(Name, Longitude, Latitudes)) :-  setof(Longitude-Name-Latitudes, pool(Name,Longitude, Latitudes), [Longitude-Name-Latitudes|_]).

% Returns an unsorted list of all the pools
listify(L) :- findall(pool(P, Longitude, Latitudes), pool(P, Longitude, Latitudes), L).

% Returns a sorted west to east list
msort(List, Sorted)    :- sort(2, @=<, List, Sorted).

% Get a tree structure with just the tree node, empty variable to store distance, list with children.
growTree([H|T], Tree, T) :- Tree = tree(H, -1,[]).

% Make a treeNode out of every pool and return as a list --> nodeify(TreeList, TreeNodeList).
nodeify([]).
nodeify([H|T]) :- assert(node(H, -1, [])), nodeify(T).
nodeify(pool(Name, Longitude, Latitudes)) :- assert(node(pool(Name, Longitude, Latitudes), -1, [])).

% addChild(Parent, Child, NewParent)
addChild(tree(P, D, C), Child, tree(P, D,[Child|C])). 

% findClosest finds a parent node for each pool node
addToClosest([]).
addToClosest([WestChild|EastFolk]) :-
    findParents(WestChild),
    addToClosest(EastFolk).

% findParent finds the all parents node for a child
findParents(Child) :-
    findall(Parent, tree(Parent, _, _), PossibleParents),
    findParent(PossibleParents, Child).

% findParent calls parentFinder to find the closest parent of the child
% findParent(Parents, Child) :- parentFinder

% Getting distance between two pools.
getDistance(pool(_, DLat1, DLon1), pool(_, DLat2, DLon2), Result):- distance(DLat1, DLon1, DLat2, DLon2, Result).
distance(DLat1, DLon1, DLat2, DLon2, Result):-  angle(DLat1, RLat1),
                                                angle(DLat2, RLat2),
                                                angle(DLon1, RLon1),
                                                angle(DLon2, RLon2),
                                                X is sin((RLat1-RLat2)/2),
                                                Y is cos(RLat1)*cos(RLat2),
                                                Z is sin((RLon1-RLon2)/2),
                                                X2 is X*X,
                                                Z2 is Z*Z,
                                                DRad is 2*asin(sqrt(X2 + Y * Z2)),
                                                Result is 6371.0 * DRad.
angle(Ad, Ar) :- Ar is pi*(Ad/180).