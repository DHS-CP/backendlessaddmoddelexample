# backendlessaddmoddelexample

SEE: sqllite-add-mod-del-example for details on modifications/improvements.

This is a forked version that is adding add/modify/delete capability using backendless as a storage engine.
Note that the UI for both backendlessaddmoddelexample and sqllite-add-mod-del-example should remain identical, except
that sqllite-add-mod-del-example uses a Primary Key, whereas backendlessaddmoddelexample uses an object id. The objectID
is a bit ugly, so trying to design similar functionality to both is a bit of a challenge.

Currently this version only saves data, it does not update/delete in backendless.


