This is my implememntation of the Cuckoo Hashing algorithim created by Dr. Rasmus Pagh and Dr. Flemming Friche Rodler. 
The original paper for Cuckoo hashing is included in the repo. It is known that for hash tables that use linear probing or external
chaining that the performance could degenerate from our expected time of O(1) to O(n). 
Cuckoo hashing is a table that has worst case performance of O(1). Although that's kind of a white lie.
We can achieve O(1) worst case time by using two backing arrays for tables and two differing hash functions.
For inserting  we use the first hash function to calculate an index and compress as a typical HashMap would. 
We then look at the first table, if the cooresponding slot is empty, we insert and we're done.
Otherwise, we push out the value that is there, like a Cuckoo bird pushing it's young out of the nest.
Then we use the second hash function and insert the pushed out item into table 2. If something is in thtat slot in table 2,
we use the first hash function to try to find it a place in table 1. 
If we concede there is no more space in the table after multiple after multiple such swaps, we perform a regrow operation.

