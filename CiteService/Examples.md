# Examples of Scala Cite Services Requests

> Assuming the server is running at, *e.g.* `localhost:9000`

## Library-Level Requests

- <http://localhost:9000/libraryinfo>

## Texts

### See the Text Catalog

- <http://localhost:9000/texts>
- <http://localhost:9000/textcatalog>
- <http://localhost:9000/textcatalog/urn:cts:greekLit:tlg0007.tlg012.Ziegler:>

### Get Valid References

- <http://localhost:9000/texts/reff/urn:cts:greekLit:tlg0007.tlg012.Ziegler:>
- <http://localhost:9000/texts/reff/urn:cts:greekLit:tlg0007.tlg012.Ziegler:2>
- <http://localhost:9000/texts/reff/urn:cts:greekLit:tlg0007.tlg012.Ziegler:2-3>
- <http://localhost:9000/texts/reff/urn:cts:greekLit:tlg0007.tlg012:1.1>

### Get Passages

- <http://localhost:9000/texts/urn:cts:greekLit:tlg0007.tlg012.Ziegler:1.1>
- <http://localhost:9000/texts/urn:cts:greekLit:tlg0007.tlg012.Ziegler:1.1-1.6>
- <http://localhost:9000/texts/urn:cts:greekLit:tlg0007.tlg012.Ziegler:2>
- <http://localhost:9000/texts/urn:cts:greekLit:tlg0007.tlg012:2.1>
- <http://localhost:9000/texts/urn:cts:greekLit:tlg0007.tlg012.Ziegler:2-3>

### NGrams
- <http://localhost:9000/texts/ngram?n=4>
- <http://localhost:9000/texts/ngram?n=4&t=2>
- [http://localhost:9000/texts/ngram?n=4&t=2&s=πολιτείᾳ](http://localhost:9000/texts/ngram?n=4&t=2&s=πολιτείᾳ)
- <http://localhost:9000/texts/ngram/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?n=3>
- [http://localhost:9000/texts/ngram/urns?ng=ἐν+τῇ+πολιτείᾳ](http://localhost:9000/texts/ngram/urns?ng=ἐν+τῇ+πολιτείᾳ)
- [http://localhost:9000/texts/ngram/urns/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?ng=ἐν+τῇ+πολιτείᾳ](http://localhost:9000/texts/ngram/urns/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?ng=ἐν+τῇ+πολιτείᾳ)
- [http://localhost:9000/texts/ngram/urns/tocorpus?ng=ἐν+τῇ+πολιτείᾳ](http://localhost:9000/texts/ngram/urns/tocorpus?ng=ἐν+τῇ+πολιτείᾳ)
- [http://localhost:9000/texts/ngram/urns/tocorpus/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?ng=ἐν+τῇ+πολιτείᾳ](http://localhost:9000/texts/ngram/urns/tocorpus/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?ng=ἐν+τῇ+πολιτείᾳ)

### String Searches
- <http://localhost:9000/texts/find?s=πολιτεί>
- <http://localhost:9000/texts/find?s=πολιτείᾳ&s=Περικλ>
- <http://localhost:9000/texts/find/urn:cts:greekLit:tlg0007.tlg012:?s=Περικλ>

### Token Searches
- [http://localhost:9000/texts/token?t=πολιτείᾳ](http://localhost:9000/texts/token?t=πολιτείᾳ)

- [http://localhost:9000/texts/token/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?t=πολιτείᾳ](http://localhost:9000/texts/token/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?t=πολιτείᾳ)

- [http://localhost:9000/texts/tokens?t=πολιτείᾳ&t=Περικλῆς](http://localhost:9000/texts/tokens?t=πολιτείᾳ&t=Περικλῆς)

- [http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?t=Περικλῆς&t=πολιτείᾳ](http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?t=Περικλῆς&t=πολιτείᾳ)

- [http://localhost:9000/texts/tokens?dist=3&t=ἀκμάζων&t=Περικλῆς](http://localhost:9000/texts/tokens?dist=3&t=ἀκμάζων&t=Περικλῆς)

- [http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?dist=3&t=ἀκμάζων&t=Περικλῆς](http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?dist=3&t=ἀκμάζων&t=Περικλῆς)

- [http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?dist=2&t=Περικλῆς&t=ἀκμάζων](http://localhost:9000/texts/tokens/urn:cts:greekLit:tlg0007.tlg012.Ziegler:?dist=2&t=Περικλῆς&t=ἀκμάζων) (should result in no citations)

## Collections of Objects

### Catalog

- <http://localhost:9000/collections/>
- <http://localhost:9000/collections/urn:cite2:ecomp:ecompDiffs.demo:>
- <http://localhost:9000/collections/reff/urn:cite2:ecomp:ecompDiffs.demo:> (just URNs)
- <http://localhost:9000/collections/hasobject/urn:cite2:ecomp:ecompDiffs.demo:2> should return `true`
- <http://localhost:9000/collections/hasobject/urn:cite2:ecomp:ecompDiffs.demo:NOTOBJECT> should return `false`
- <http://localhost:9000/collections/labelmap> (returns a map of Cite2Urn -> String, the label of each citable object)

### Objects

- <http://localhost:9000/objects/urn:cite2:ecomp:ecompDiffs.demo:>
- <http://localhost:9000/objects/urn:cite2:ecomp:ecompDiffs.demo:1>
- <http://localhost:9000/objects/prevurn/urn:cite2:ecomp:ecompDiffs.demo:2>
- <http://localhost:9000/objects/nexturn/urn:cite2:ecomp:ecompDiffs.demo:2>
- <http://localhost:9000/objects/urn:cite2:ecomp:ecompDiffs.demo:2-3>
- <http://localhost:9000/objects/paged/urn:cite2:ecomp:ecompDiffs.demo:?offset=1&limit=10>
- <http://localhost:9000/objects/paged/urn:cite2:ecomp:ecompDiffs.demo:?offset=11&limit=10>
- <http://localhost:9000/objects/paged/urn:cite2:ecomp:ecompDiffs.demo:>


### Finding Objects

urn-match

- <http://localhost:9000/objects/find/urnmatch?find=urn:cts:greekLit:tlg0007.tlg012.Ziegler:2.1>
- <http://localhost:9000/objects/find/urnmatch?find=urn:cts:greekLit:tlg0007.tlg012.Ziegler:2&offset=1&limit=2> (`offset` and `limit` allow paged results; this applies to all searching requests.)

regexmatch

- [http://localhost:9000/objects/find/regexmatch?find=[MT]](http://localhost:9000/objects/find/regexmatch?find=[MT]) (Find differences with type `M` or `T T`.)

valueequals



- <http://localhost:9000/objects/find/valueequals?propertyurn=urn:cite2:ecomp:ecompDiffs.demo.seq:&value=3>

numeric less-than

- <http://localhost:9000/objects/find/numeric?n1=1&op=lt> (Find significant differences.)
- <http://localhost:9000/objects/find/numeric?n1=1&op=lt&propertyurn=urn:cite2:ecomp:ecompDiffs.demo.distance:>




