#Integratin Build#

A Scala build-environment demonstrating translation between JSON output from eComparatio to a CEX library of texts, collections, and relations.

## Contents

### Sample JSON

`resources/PeriklesFinal.json`: Sample JSON output from eComparatio tool (<https://github.com/ecomp-shONgit/ecomparatio>).

### Test Script

`scripts/ecomparatio.sc`: Experimental Scala script for parsing eComparatio JSON into citable texts. Run with:

~~~
   $ sbt
   $ ++2.12.3
   $ crossedJVM/console
   $ :load scripts/ecomparatio.sc
~~~

Look for output in `resources/`.

### CEX Output

The `scripts/ecomparatio.sc` script will read in the JSON output from eComparatio, which aligns two texts, and output the results as a CEX file, a plain-text serialization of data following the [CITE Architecture](http://cite-architecture.github.io).

The output will be in `resources/plutarchComparatio.cex`.

The output will include:

1. CTS texts of both compared works, citable at the version level.
1. CTS "analytical exemplars" of both texts, citable at the word-token level.
1. A CITE Collection capturing a comprehensive, token-level `diff` operation on both texts (`urn:cite2:ecomp:ecompDiffs.demo:`): `resources/plutarchComparatio.cex`
1. CiteRelations records for all significant diffs (`distance < 1.0`).

The CITE Collection:

| Property | Type | Notes |
|----------|-------|------|
| `urn` | Cite2Urn | Makes this particular difference, itself, citable |
| `basetext` | Cite2Urn | exemplar-level URN, citing the token |
| `othertext` | Cite2Urn | exemplar-level URN, citing the token |
| `label` | String | user-displayed label for this diff; can be anything |
| `seq` | Number | The token-sequence according to the text-order of `basetext` |
| `difftype` | String | The type of difference. Could be made into a controlled vocabulary type |
| `distance` | The  Levenshtein edit-distance 



