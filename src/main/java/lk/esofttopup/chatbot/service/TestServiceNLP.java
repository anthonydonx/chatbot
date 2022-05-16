package lk.esofttopup.chatbot.service;


import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TestServiceNLP {
    public static String text = "Marie was born in Paris.";
//    String paragraph = "This is a statement. This is another statement. "
//            + "This is an abstract word for time, "
//            + "that is always flying. My email address is google@gmail.com."
//            + " i'm Tony,from Sri Lanka SMS, MMS, FM";

    //String paragraph="My mobile features are SMS,MMS and FM.";
    //String paragraph="Domestic use";
    String paragraph = "My name is";


    public List<String> detectSentence() throws IOException {
        //InputStream is = getClass().getResourceAsStream("/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin");
        InputStream is = getClass().getResourceAsStream("/en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME detectorME = new SentenceDetectorME(model);
        String[] strings = detectorME.sentDetect(paragraph);
        for (String sentence : strings
        ) {
            System.out.println(sentence);
        }
        return Arrays.stream(strings).collect(Collectors.toList());
    }


    public String[] tokenize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizerME = new TokenizerME(model);
        String[] tokenize = tokenizerME.tokenize(paragraph);
        Arrays.stream(tokenize).forEach(token -> log.info("Token found :{}", token));
        return tokenize;
    }


    public void detectEntities() throws IOException {
        String[] tokenArray = tokenize();

        InputStream isDate = getClass().getResourceAsStream("/finder/en-ner-date.bin");
        InputStream isLocation = getClass().getResourceAsStream("/finder/en-ner-location.bin");
        InputStream isMoney = getClass().getResourceAsStream("/finder/en-ner-money.bin");
        InputStream isOrganization = getClass().getResourceAsStream("/finder/en-ner-organization.bin");
        InputStream isPercentage = getClass().getResourceAsStream("/finder/en-ner-percentage.bin");
        InputStream isPersion = getClass().getResourceAsStream("/finder/en-ner-person.bin");
        InputStream isTime = getClass().getResourceAsStream("/finder/en-ner-time.bin");


        TokenNameFinderModel modelDate = new TokenNameFinderModel(isDate);
        TokenNameFinderModel modelLocation = new TokenNameFinderModel(isLocation);
        TokenNameFinderModel modelMoney = new TokenNameFinderModel(isMoney);
        TokenNameFinderModel modelOrganization = new TokenNameFinderModel(isOrganization);
        TokenNameFinderModel modelPercentage = new TokenNameFinderModel(isPercentage);
        TokenNameFinderModel modelPerson = new TokenNameFinderModel(isPersion);
        TokenNameFinderModel modelTime = new TokenNameFinderModel(isTime);

        NameFinderME dateFinder = new NameFinderME(modelDate);
        NameFinderME locationFinder = new NameFinderME(modelLocation);
        NameFinderME moneyFinder = new NameFinderME(modelMoney);
        NameFinderME organizationFinder = new NameFinderME(modelOrganization);
        NameFinderME percentageFinder = new NameFinderME(modelPercentage);
        NameFinderME personFinder = new NameFinderME(modelPerson);
        NameFinderME timeFinder = new NameFinderME(modelTime);


        Span[] dateSpans = dateFinder.find(tokenArray);
        Span[] locationSpans = locationFinder.find(tokenArray);
        Span[] moneySpans = moneyFinder.find(tokenArray);
        Span[] organizationSpans = organizationFinder.find(tokenArray);
        Span[] percentageSpans = percentageFinder.find(tokenArray);
        Span[] personSpans = personFinder.find(tokenArray);
        Span[] timeSpans = timeFinder.find(tokenArray);

        log.info("Date :{}, Location :{}, Money :{}, Organization :{}, Percentage :{}, Person :{}, Time :{}", Arrays.stream(dateSpans), locationSpans, moneySpans, organizationSpans, percentageSpans, personSpans, timeSpans);

    }

    //@PostConstruct

    public String[] posTag() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/en-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStream);
        POSTaggerME posTaggerME = new POSTaggerME(posModel);
        String[] tag = posTaggerME.tag(tokenize());
        Arrays.stream(tag).forEach(t -> log.info("Tag :{}", t));
        return tag;


    }

    String[] lemmatize() throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/en-lemmatizer.dict");
        // loading the lemmatizer with dictionary
        DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(resourceAsStream);

        // finding the lemmas
        String[] lemmas = lemmatizer.lemmatize(tokenize(), posTag());

        // printing the results
        System.out.println("\nPrinting lemmas for the given sentence...");
        System.out.println("WORD -POSTAG : LEMMA");
        return lemmas;
    }

    public DoccatModel trainCategories() {
        DoccatModel model = null;
        URL dataUrl = getClass().getResource("/train2.txt");
        String outputUrl = "/Users/anthonydonx/IdeaProjects/chatbot_data/";

        try {

            MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(
                    new File(dataUrl.toURI()));

            // parameters used by machine learning algorithm, Maxent, to train its weights
            TrainingParameters mlParams = new TrainingParameters();
            mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(15));
            mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));


            ObjectStream<String> lineStream = new PlainTextByLineStream(
                    factory, "UTF-8");

            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(
                    lineStream);

            model = DocumentCategorizerME.train("en", sampleStream,
                    TrainingParameters.defaultParams(), new DoccatFactory());

            OutputStream modelOut = null;

            File modelFileTmp = new File(outputUrl + File.separator + "data.bin");
            modelOut = new BufferedOutputStream(new FileOutputStream(
                    modelFileTmp));
            model.serialize(modelOut);


            log.info("Model created");
            return model;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


        return model;
    }


    public DoccatModel trainCategoryDataset() {
        DoccatModel model = null;
        //URL dataUrl= getClass().getResource("/to_train/trainingDataSentences.txt");
        URL dataUrl = getClass().getResource("/to_train/train.text");
        String outputUrl = "/Users/anthonydonx/IdeaProjects/chatbot_data/";

        try {

            MarkableFileInputStreamFactory fileInputStreamFactory = new MarkableFileInputStreamFactory(
                    new File(dataUrl.toURI()));
            ObjectStream<String> lineStream =
                    new PlainTextByLineStream(fileInputStreamFactory, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters mlParams = new TrainingParameters();
            mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(15));
            mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));

            DoccatFactory factory = new DoccatFactory(new FeatureGenerator[]{new BagOfWordsFeatureGenerator()});

            TrainingParameters parameters = ModelUtil.createDefaultTrainingParameters();
            parameters.put(TrainingParameters.CUTOFF_PARAM, 5);
            parameters.put(TrainingParameters.ITERATIONS_PARAM, 0);

            model = DocumentCategorizerME.train("en", sampleStream, mlParams, new DoccatFactory());
            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return model;
    }

    //@PostConstruct
    String categorize() throws IOException {
        DocumentCategorizerME categorizerME = new DocumentCategorizerME(trainCategoryDataset());
        for (int i = 0; i < categorizerME.getNumberOfCategories(); i++) {
            log.info("Trained Categories : {}", categorizerME.getCategory(i));
        }
        double[] categorize = categorizerME.categorize(tokenize());
        String bestCategory = categorizerME.getBestCategory(categorize);
        Arrays.sort(categorize);

        log.info("All categories , {} ", categorizerME.getAllResults(categorize));

        SortedMap<Double, Set<String>> doubleSetSortedMap = categorizerME.sortedScoreMap(tokenize());

        doubleSetSortedMap.forEach((aDouble, strings) -> log.info("Value is : {} & Categories : {}", aDouble, strings));

        // log.info("All categories : {}",categorize);
        log.info("Best Category : {}", bestCategory);
        return bestCategory;

    }

    //@PostConstruct
    public void testDataLoading() {
        ObjectStream<DocumentSample> sampleObjectStream = ObjectStreamUtils.createObjectStream(
                new DocumentSample("1", new String[]{"a", "b", "c"}),
                new DocumentSample("1", new String[]{"a", "b", "c", "1", "2"}),
                new DocumentSample("1", new String[]{"a", "b", "c", "3", "4"}),
                new DocumentSample("0", new String[]{"x", "y", "z"}),
                new DocumentSample("0", new String[]{"x", "y", "z", "5", "6"}),
                new DocumentSample("0", new String[]{"x", "y", "z", "7", "8"}));

        try {
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 0);
            DoccatModel model = DocumentCategorizerME.train("x-unspecified", sampleObjectStream,
                    params, new DoccatFactory());
            DocumentCategorizer doccat = new DocumentCategorizerME(model);
            double[] aProbs = doccat.categorize(new String[]{"a"});
            log.info("Best category is : {}", doccat.getBestCategory(aProbs));

            double[] bProbs = doccat.categorize(new String[]{"x"});
            log.info("Best category is : {}", doccat.getBestCategory(bProbs));

            log.info("Data trained successfully");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("File not found", e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("I/O Exception ", e);
        }
    }
    //@PostConstruct
    public void stanfordSentenceDetect() {
       /* Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        properties.setProperty("coref.algorithm", "neural");
        StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP();
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        CoreDocument document = new CoreDocument(paragraph);
        pipeline.annotate(document);

        List<CoreSentence> sentences = document.sentences();
        sentences.stream().forEach(s -> System.out.println(s));*/


        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        CoreDocument document = pipeline.processToCoreDocument(text);
    }
}
