CREATE CONSTRAINT ON (node:Article) ASSERT (node.name) IS UNIQUE;
CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
UNWIND [{_id:0, properties:{name:"Ian Goodfellow"}}, {_id:2, properties:{name:"Donald Ervin Knuth"}}, {_id:3, properties:{name:"Yinghui Huang"}}, {_id:5, properties:{name:"Guanyu Li"}}, {_id:7, properties:{name:"Alec Radford"}}, {_id:8, properties:{name:"Luke Metz"}}, {_id:9, properties:{name:"Soumith Chintala"}}, {_id:11, properties:{name:"Stefano Abbate"}}, {_id:12, properties:{name:"Jyrki Katajainen"}}, {_id:13, properties:{name:"Tero Karras"}}, {_id:14, properties:{name:"Samuli Laine"}}, {_id:15, properties:{name:"Marco Avvenuti"}}, {_id:16, properties:{name:"Timo Aila"}}, {_id:18, properties:{name:"Francesco Bonatesta"}}, {_id:19, properties:{name:"Paolo Corsini"}}, {_id:20, properties:{name:"Amr Elmasry"}}, {_id:22, properties:{name:"Alessio Vecchio"}}, {_id:25, properties:{name:"Edoardo Giacomello"}}, {_id:26, properties:{name:"Pier Luca Lanzi"}}, {_id:27, properties:{name:"Jesper Larsson Träff"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Author;
UNWIND [{_id:28, properties:{name:"Daniele Loiacono"}}, {_id:29, properties:{name:"Lingmei Ren "}}, {_id:32, properties:{name:"Yanjun Peng"}}, {_id:34, properties:{name:"Vanessa Volz"}}, {_id:36, properties:{name:"Ekachai Thammasat"}}, {_id:37, properties:{name:"Conrado Martinez"}}, {_id:39, properties:{name:"Sebastian Wild"}}, {_id:40, properties:{name:"Steve Dahlskog"}}, {_id:41, properties:{name:"Julian Togelius"}}, {_id:43, properties:{name:"Natthapon Pannurat"}}, {_id:44, properties:{name:"Markus Nebel"}}, {_id:46, properties:{name:"Surapa Thiemjarus"}}, {_id:47, properties:{name:"Ekawit Nantajeewarawat"}}, {_id:49, properties:{name:"Paul Biggar"}}, {_id:50, properties:{name:"David Gregg"}}, {_id:52, properties:{name:"Stefan Edelkamp"}}, {_id:53, properties:{name:"Armin Weiss"}}, {_id:55, properties:{name:"Gerth Stölting Brodal"}}, {_id:56, properties:{name:"Gabriel Moruz"}}, {_id:59, properties:{name:"Peter Sanders"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Author;
UNWIND [{_id:60, properties:{name:"Kanela Kaligosi"}}, {_id:62, properties:{name:"Sebastian Winkel"}}, {_id:64, properties:{name:"Paul Virak Khuong"}}, {_id:65, properties:{name:"Pat Morin"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Author;
UNWIND [{name:"Generative Adversarial Nets", properties:{year:2014, topics:["Machine Learning", "GAN"]}}, {name:"The Art of Computer Programming", properties:{year:1997, topics:[]}}, {name:"A Semantic Analysis for Internet of Things", properties:{year:2010, topics:["Internet of Things", "product information", "Internet application", "ontology", "Semantic Web"]}}, {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks", properties:{year:2016, topics:["Machine Learning", "GAN"]}}, {name:"A Style-Based Generator Architecture for Generative Adversarial Networks", properties:{year:2019, topics:["Machine Learning", "GAN", "StyleGAN"]}}, {name:"Lean Programs, Branch Mispredictions, and Sorting", properties:{year:2012, topics:[]}}, {name:"Branch Mispredictions Don’t Affect Mergesort", properties:{year:2012, topics:[]}}, {name:"A smartphone-based fall detection system", properties:{year:2012, topics:["Pervasive healthcare", "Fall detection", "Wearable sensors"]}}, {name:"A Meticulous Analysis Of Mergesort Programs", properties:{year:1997, topics:[]}}, {name:"DOOM Level Generation using Generative Adversarial Networks", properties:{year:2018, topics:["Machine Learning", "GAN", "DOOM"]}}, {name:"Research of Fall Detection and Fall Prevention Technologies: A Systematic Review", properties:{year:2019, topics:["Fall detection", "fall prevention"]}}, {name:"Evolving Mario Levels in the Latent Space of a DeepConvolutional Generative Adversarial Network", properties:{year:2018, topics:["Machine Learning", "GAN", "games", "mario"]}}, {name:"The Statistical Recognition of Walking, Jogging, and Running Using Smartphone Accelerometers", properties:{year:2013, topics:["Accelerometer", "walking", "jogging", "running"]}}, {name:"Linear levels through n-grams", properties:{year:2016, topics:["Machine Learning", "n-grams"]}}, {name:"Analysis of Branchmisses in Quicksort", properties:{year:2015, topics:[]}}, {name:"Automatic Fall Monitoring: A Review", properties:{year:2014, topics:["fall monitoring", "fall detection", "fall prevention", "wireless sensors", "wearable sensors"]}}, {name:"An Experimental Study of Sorting and Branch Prediction", properties:{year:2008, topics:[]}}, {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort", properties:{year:2016, topics:[]}}, {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms", properties:{year:2005, topics:[]}}, {name:"Skewed Binary Search Trees", properties:{year:2006, topics:[]}}] AS row
CREATE (n:Article{name: row.name}) SET n += row.properties;
UNWIND [{name:"How Branch Mispredictions Affect Quicksort", properties:{year:2006, topics:[]}}, {name:"Super Scalar Sample Sort", properties:{year:2004, topics:[]}}, {name:"Array Layouts for Comparison-Based Searching", properties:{year:2017, topics:[]}}, {name:"Sorting in the Presence of Branch Prediction and Caches", properties:{year:2008, topics:[]}}, {name:"Branchless Search Programs", properties:{year:2013, topics:[]}}] AS row
CREATE (n:Article{name: row.name}) SET n += row.properties;
UNWIND [{start: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, end: {name:"Generative Adversarial Nets"}, properties:{}}, {start: {name:"A Style-Based Generator Architecture for Generative Adversarial Networks"}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {name:"A Style-Based Generator Architecture for Generative Adversarial Networks"}, end: {name:"Generative Adversarial Nets"}, properties:{}}, {start: {name:"A smartphone-based fall detection system"}, end: {name:"A Semantic Analysis for Internet of Things"}, properties:{}}, {start: {name:"DOOM Level Generation using Generative Adversarial Networks"}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {name:"Research of Fall Detection and Fall Prevention Technologies: A Systematic Review"}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {name:"Evolving Mario Levels in the Latent Space of a DeepConvolutional Generative Adversarial Network"}, end: {name:"Generative Adversarial Nets"}, properties:{}}, {start: {name:"Evolving Mario Levels in the Latent Space of a DeepConvolutional Generative Adversarial Network"}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {name:"The Statistical Recognition of Walking, Jogging, and Running Using Smartphone Accelerometers"}, end: {name:"A Semantic Analysis for Internet of Things"}, properties:{}}, {start: {name:"The Statistical Recognition of Walking, Jogging, and Running Using Smartphone Accelerometers"}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {name:"DOOM Level Generation using Generative Adversarial Networks"}, end: {name:"Linear levels through n-grams"}, properties:{}}, {start: {name:"Automatic Fall Monitoring: A Review"}, end: {name:"The Statistical Recognition of Walking, Jogging, and Running Using Smartphone Accelerometers"}, properties:{}}, {start: {name:"Automatic Fall Monitoring: A Review"}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {name:"Automatic Fall Monitoring: A Review"}, end: {name:"A Semantic Analysis for Internet of Things"}, properties:{}}, {start: {name:"Branchless Search Programs"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}, {start: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, end: {name:"A Meticulous Analysis Of Mergesort Programs"}, properties:{}}, {start: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}] AS row
MATCH (start:Article{name: row.start.name})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:References]->(end) SET r += row.properties;
UNWIND [{start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"Skewed Binary Search Trees"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"A Meticulous Analysis Of Mergesort Programs"}, properties:{}}, {start: {name:"Branch Mispredictions Don’t Affect Mergesort"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"A Meticulous Analysis Of Mergesort Programs"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"Analysis of Branchmisses in Quicksort"}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}, {start: {name:"Analysis of Branchmisses in Quicksort"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"Analysis of Branchmisses in Quicksort"}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}, {start: {name:"An Experimental Study of Sorting and Branch Prediction"}, end: {name:"Sorting in the Presence of Branch Prediction and Caches"}, properties:{}}, {start: {name:"An Experimental Study of Sorting and Branch Prediction"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"An Experimental Study of Sorting and Branch Prediction"}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}, {start: {name:"An Experimental Study of Sorting and Branch Prediction"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"An Experimental Study of Sorting and Branch Prediction"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"Branch Mispredictions Don’t Affect Mergesort"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}] AS row
MATCH (start:Article{name: row.start.name})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:References]->(end) SET r += row.properties;
UNWIND [{start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"Analysis of Branchmisses in Quicksort"}, properties:{}}, {start: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"Skewed Binary Search Trees"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"Skewed Binary Search Trees"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"How Branch Mispredictions Affect Quicksort"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"How Branch Mispredictions Affect Quicksort"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"How Branch Mispredictions Affect Quicksort"}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {name:"Super Scalar Sample Sort"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"Array Layouts for Comparison-Based Searching"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"Array Layouts for Comparison-Based Searching"}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {name:"Sorting in the Presence of Branch Prediction and Caches"}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {name:"Branchless Search Programs"}, end: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, properties:{}}, {start: {name:"Branchless Search Programs"}, end: {name:"Branch Mispredictions Don’t Affect Mergesort"}, properties:{}}, {start: {name:"Branchless Search Programs"}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}] AS row
MATCH (start:Article{name: row.start.name})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:References]->(end) SET r += row.properties;
UNWIND [{start: {_id:2}, end: {name:"The Art of Computer Programming"}, properties:{}}, {start: {_id:3}, end: {name:"A Semantic Analysis for Internet of Things"}, properties:{}}, {start: {_id:5}, end: {name:"A Semantic Analysis for Internet of Things"}, properties:{}}, {start: {_id:9}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {_id:8}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {_id:7}, end: {name:"Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks"}, properties:{}}, {start: {_id:13}, end: {name:"A Style-Based Generator Architecture for Generative Adversarial Networks"}, properties:{}}, {start: {_id:14}, end: {name:"A Style-Based Generator Architecture for Generative Adversarial Networks"}, properties:{}}, {start: {_id:16}, end: {name:"A Style-Based Generator Architecture for Generative Adversarial Networks"}, properties:{}}, {start: {_id:12}, end: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, properties:{}}, {start: {_id:20}, end: {name:"Lean Programs, Branch Mispredictions, and Sorting"}, properties:{}}, {start: {_id:20}, end: {name:"Branch Mispredictions Don’t Affect Mergesort"}, properties:{}}, {start: {_id:12}, end: {name:"Branch Mispredictions Don’t Affect Mergesort"}, properties:{}}, {start: {_id:11}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {_id:15}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {_id:18}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {_id:19}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {_id:22}, end: {name:"A smartphone-based fall detection system"}, properties:{}}, {start: {_id:12}, end: {name:"A Meticulous Analysis Of Mergesort Programs"}, properties:{}}, {start: {_id:0}, end: {name:"Generative Adversarial Nets"}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:Wrote]->(end) SET r += row.properties;
UNWIND [{start: {_id:27}, end: {name:"A Meticulous Analysis Of Mergesort Programs"}, properties:{}}, {start: {_id:25}, end: {name:"DOOM Level Generation using Generative Adversarial Networks"}, properties:{}}, {start: {_id:26}, end: {name:"DOOM Level Generation using Generative Adversarial Networks"}, properties:{}}, {start: {_id:28}, end: {name:"DOOM Level Generation using Generative Adversarial Networks"}, properties:{}}, {start: {_id:29}, end: {name:"Research of Fall Detection and Fall Prevention Technologies: A Systematic Review"}, properties:{}}, {start: {_id:32}, end: {name:"Research of Fall Detection and Fall Prevention Technologies: A Systematic Review"}, properties:{}}, {start: {_id:34}, end: {name:"Evolving Mario Levels in the Latent Space of a DeepConvolutional Generative Adversarial Network"}, properties:{}}, {start: {_id:36}, end: {name:"The Statistical Recognition of Walking, Jogging, and Running Using Smartphone Accelerometers"}, properties:{}}, {start: {_id:40}, end: {name:"Linear levels through n-grams"}, properties:{}}, {start: {_id:41}, end: {name:"Linear levels through n-grams"}, properties:{}}, {start: {_id:37}, end: {name:"Analysis of Branchmisses in Quicksort"}, properties:{}}, {start: {_id:39}, end: {name:"Analysis of Branchmisses in Quicksort"}, properties:{}}, {start: {_id:44}, end: {name:"Analysis of Branchmisses in Quicksort"}, properties:{}}, {start: {_id:43}, end: {name:"Automatic Fall Monitoring: A Review"}, properties:{}}, {start: {_id:46}, end: {name:"Automatic Fall Monitoring: A Review"}, properties:{}}, {start: {_id:47}, end: {name:"Automatic Fall Monitoring: A Review"}, properties:{}}, {start: {_id:49}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}, {start: {_id:50}, end: {name:"An Experimental Study of Sorting and Branch Prediction"}, properties:{}}, {start: {_id:52}, end: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, properties:{}}, {start: {_id:53}, end: {name:"BlockQuicksort Avoiding Branch Mispredictions in Quicksort"}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:Wrote]->(end) SET r += row.properties;
UNWIND [{start: {_id:55}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {_id:56}, end: {name:"Tradeoffs Between Branch Mispredictions and Comparisons for Sorting Algorithms"}, properties:{}}, {start: {_id:55}, end: {name:"Skewed Binary Search Trees"}, properties:{}}, {start: {_id:56}, end: {name:"Skewed Binary Search Trees"}, properties:{}}, {start: {_id:59}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}, {start: {_id:60}, end: {name:"How Branch Mispredictions Affect Quicksort"}, properties:{}}, {start: {_id:59}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {_id:62}, end: {name:"Super Scalar Sample Sort"}, properties:{}}, {start: {_id:64}, end: {name:"Array Layouts for Comparison-Based Searching"}, properties:{}}, {start: {_id:65}, end: {name:"Array Layouts for Comparison-Based Searching"}, properties:{}}, {start: {_id:49}, end: {name:"Sorting in the Presence of Branch Prediction and Caches"}, properties:{}}, {start: {_id:50}, end: {name:"Sorting in the Presence of Branch Prediction and Caches"}, properties:{}}, {start: {_id:12}, end: {name:"Branchless Search Programs"}, properties:{}}, {start: {_id:20}, end: {name:"Branchless Search Programs"}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:Article{name: row.end.name})
CREATE (start)-[r:Wrote]->(end) SET r += row.properties;
MATCH (n:`UNIQUE IMPORT LABEL`)  WITH n LIMIT 20000 REMOVE n:`UNIQUE IMPORT LABEL` REMOVE n.`UNIQUE IMPORT ID`;
DROP CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
