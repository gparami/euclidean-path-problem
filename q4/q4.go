package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"strings"
	"strconv"
	"sort"
)

func main() {
	//read file, exits program if error
	bFile, err := ioutil.ReadFile("database.txt")
	if err != nil {
		fmt.Print(err)
		os.Exit(1)
	}

	//splits file into a slice of pool info strings
	strPools := strings.Split(string(bFile), "\n")

	//slice of pointers to pool structures
	var pools []*Pool

	//adding pools to pools slice
	for _, e := range strPools {
		slcPool := strings.Split(e,",")
		poolName := strings.Replace(slcPool[0], "\"", "", -1)
		poolLongitude, lonErr := strconv.ParseFloat(slcPool[1], 64)
		poolLatitude, latErr := strconv.ParseFloat(slcPool[2], 64)
		if poolName == "" || lonErr != nil || latErr != nil { fmt.Printf("Contents of Database.txt is not properly formatted!"); os.Exit(2)}
		pools = append(pools, newPool(poolName, poolLongitude, poolLatitude))
	}

	//sort pools by longitude
	sort.Slice(pools, func(i, j int) bool { return pools[i].longitude < pools[j].longitude })

	//creating tree
	root, pools := pools[0], pools[1:]
	tree := plantTree(root)

	//sort pools by distance to root
	for _, pool := range pools { pool.setDistanceToRoot(pool.distanceFrom(root)) }
	sort.Slice(pools, func(i, j int) bool { return pools[i].distanceToRoot < pools[j].distanceToRoot })

	//build tree
	for len(pools) > 0 {
		child := pools[0]
		trees := tree.getTreeList() //get the tree as a list
		for _, t := range trees { t.pool.setDistanceToRoot(t.pool.distanceFrom(child)) }
		sort.Slice(trees, func(i, j int) bool { return trees[i].pool.distanceToRoot < trees[j].pool.distanceToRoot }) //sort it by the closest to child
		trees[0].addChild(plantTree(child)) //add the child to it as a tree
		pools = pools[1:] //delete child
	}

	//prepare solution
	pools = tree.getPreOrder()
	var strSolution string
	var distanceTravelled float64 = 0
	for i, pool := range pools {
		strSolution += pool.name + " " + strconv.FormatFloat(distanceTravelled, 'E', -1, 64) + "\n"
		if (i+1) < len(pools) { distanceTravelled += pool.distanceFrom(pools[i+1]) }
	}

	// write the solution
	fmt.Print(strSolution)
	err = ioutil.WriteFile("solution.txt", []byte(strSolution), 0644)
	if err != nil {
		panic("error writing the solution")
	}
}

//Tree Structure
type Tree struct { pool *Pool; children []*Tree; parent *Tree }
func plantTree(pool *Pool) (root *Tree) { return &Tree{pool, nil, nil} }
func (parent *Tree) setPool(pool *Pool) { parent.pool = pool }
func (node *Tree) setParent(parent *Tree) { node.parent = parent }
func (parent *Tree) addChild(child *Tree) { parent.children = append(parent.children, child) }
func (root *Tree) getPreOrder() (preOrderList []*Pool) { buildPreOrder(root, &preOrderList); return preOrderList }
func (root *Tree) getTreeList() (tree []*Tree) { buildTreeList(root, &tree); return tree }
func buildPreOrder(node *Tree, preOrderList *[]*Pool) {
	*preOrderList = append(*preOrderList, node.pool)
	for _, elem := range node.children {
		buildPreOrder(elem, preOrderList)
	}
}
func buildTreeList(node *Tree, tree *[]*Tree) {
	*tree = append(*tree, node)
	for _, elem := range node.children {
		buildTreeList(elem, tree)
	}
}

//Pool Structure
type Pool struct { name string; longitude, latitude, distanceToParent, distanceToRoot float64 }
func newPool(name string, longitude, latitude float64) *Pool { return &Pool{name, longitude, latitude, 0, 0} }
func (pool *Pool) setDistanceToParent(distanceToParent float64) { pool.distanceToParent = distanceToParent }
func (pool *Pool) setDistanceToRoot(distanceToRoot float64) { pool.distanceToRoot = distanceToRoot }
func (pool *Pool) distanceFrom(other *Pool) float64 {
	x1 := pool.latitude * math.Pi / 180
	x2 := other.latitude * math.Pi / 180
	z1 := pool.longitude * math.Pi / 180
	z2 := other.longitude * math.Pi / 180
	x := math.Pow(math.Sin((x1-x2)/2), 2)
	y := math.Cos(x1) * math.Cos(x2)
	z := math.Pow(math.Sin((z1-z2)/2), 2)
	dRadians := 2 * math.Asin(math.Sqrt(x+y*z))
	return 6371.0 * dRadians
}