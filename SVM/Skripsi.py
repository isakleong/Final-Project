from os import listdir
import pandas as pd

#load dataset function
def load_dataset(filename):

    df = pd.read_csv(filename, encoding='ISO-8859-1')
    df.head()

    print(df.shape)
    print(df.info)

    col = ['Biro / Unit', 'Teks_Aspirasi']
    df= df[col]
    df= df[pd.notnull(df['Teks_Aspirasi'])]
    
    df.columns=['Biro / Unit', 'Teks_Aspirasi']
    df['category_aspirasi_id'] = df['Biro / Unit'].factorize()[0]
    from io import StringIO
    category_id_df = df[['Biro / Unit', 'category_aspirasi_id']].drop_duplicates().sort_values('category_aspirasi_id')
    category_to_id = dict(category_id_df.values)
    id_to_category = dict(category_id_df[['category_aspirasi_id', 'Biro / Unit']].values)
    
    df.head()

    return df

#graphics of class distribution function
def load_distribution_graphic(df):
    import matplotlib.pyplot as plt
    from matplotlib import pyplot
    
    #bar graphic
    fig = plt.figure(figsize=(10,8))
    df.groupby('Biro / Unit').Teks_Aspirasi.count().plot.bar(ylim=0)
    plt.show()
    
    #historgram graphic
    df.hist()
    pyplot.show()
    
    # 0: puskom, 1: poli, 2: uppk, 3: baka, 4: ukk, 5: other, 6: baak

#counting class distribution funtion
def get_class_distribution(df):
    from collections import Counter
    target_class = df.values[:,-1]
    data_class = Counter(target_class)
    for i,j in data_class.items():
        per = j / len(target_class) * 100
        prop = round(j / len(target_class), 8)
        print('class: %s, count: %d, prop: %f, percentage: %.3f%%' % (i, j, prop, per))

# TEXT PREPROCESSING
def clean_text(arr_text, option):
    import re
    import nltk
    from nltk.tokenize import word_tokenize
    from nltk.util import ngrams
    from nltk.tokenize import sent_tokenize, word_tokenize
    from nltk.corpus import stopwords
    from Sastrawi.Stemmer.StemmerFactory import StemmerFactory
    from Sastrawi.StopWordRemover.StopWordRemoverFactory import StopWordRemoverFactory, StopWordRemover, ArrayDictionary

    #case folding function
    def caseFolding(text):
        #convert to lowercase
        text = text.lower()
        return text
    
    #punctuation removal for non normalization data
    def removePunctuation(text):
        #remove special tags
        text = re.sub("<!--?.*?-->","",text)

        #remove digits and special chars
        text = re.sub('(\\d|\\W)+'," ",text)

        #remove single chars
        text = text.split()
        text = [word for word in text if len(word) > 1]
        text = concateText(text)

        return text

    #punctuation removal function
    def punctuationRemoval(text):
        #remove special tags
        text = re.sub("<!--?.*?-->","",text)

        #remove punctuation mark without aposthrope
        text = re.sub(r'[^\w"]+',' ',text)

        # text = re.sub('(\\d|\\W)+'," ",text)
        # text = text.split()
        # text = [word for word in text if len(word) > 1]
        # text = concateText(text)

        return text
    
    def tokenPunctuation(text):
        #remove single character
        text = text.split()
        text = [word for word in text if len(word) > 1]
        text = concateText(text)

        #remove digits
        text = re.sub('(\\d|\\W)+'," ",text)

        return text
    
    #text tokenization function
    def textTokenization(text):
        #splitting text into tokens
        tokens = word_tokenize(text)

        return tokens
    
    #text normalization function
    def textNormalization(text):
        dirt_arr = []
        clean_arr = []
        regex_arr = []
        #read dirt data
        with open('Dataset/normalization_dirt.txt', 'r') as f:
            content = f.read().splitlines()
            for dirt_word in content:
                dirt_arr.append(dirt_word)
    
        #read clean data
        with open('Dataset/normalization_clean.txt', 'r') as f:
            content = f.read().splitlines()
            for dirt_word in content:
                clean_arr.append(dirt_word)

        #read regex data
        with open('Dataset/regex_normalization.txt', 'r') as f:
            content = f.read().splitlines()
            for dirt_word in content:
                regex_arr.append(dirt_word)
    
        #normalization process
        new_text = []

        #handling (...)(-)(nya)
        match_regex = re.findall(r'[\w]+[-]nya', text)
        for match_word in match_regex:
            remove_whitespace = re.sub("-nya", "", match_word)
            text = re.sub(match_word, remove_whitespace, text)
        
        #regex for finding word inside quote ("bla bla") -- change just to bla bla
        match_regex = re.findall(r'"\b([^"]*)"',text)
        #deleting match regex
        for match in match_regex:
            replace = re.sub('"'+match+'"', match, text)
            replace = " ".join(replace.split())
            text =  replace
        
        #regex replace english substring
        substr_dirt_arr = []
        substr_clean_arr = []
        #read dirt data
        with open('Dataset/regex_substring_normalization_in.txt', 'r') as f:
            content = f.read().splitlines()
            for dirt_word in content:
                substr_dirt_arr.append(dirt_word)
    
        #read clean data
        with open('Dataset/regex_substring_normalization_out.txt', 'r') as f:
            content = f.read().splitlines()
            for dirt_word in content:
                substr_clean_arr.append(dirt_word)
        
        #fix regex substring english
        for item in substr_dirt_arr:
            #check contain regex substr or not
            match_substr = re.search(item, text)
            if(match_substr):
                dirt_index = substr_dirt_arr.index(item)
                str_replace = substr_clean_arr[dirt_index]
                text = re.sub(r'\b'+item+r'\b', str_replace, text)

        #typo checking
        for word in text.split():
            #checking if word contain aposthrope (org")
            custom_regex_aps = re.search(r'[^"\s]*[a-z]+["]', word)
            #checking if word contain number (org2)
            custom_regex_num = re.search(r'[^"\s]*[a-z]+[2]', word)

            if(custom_regex_aps or custom_regex_num):
                if(custom_regex_aps):
                    custom_regex = custom_regex_aps.group()
                else:
                    custom_regex = custom_regex_num.group()

                #if a typo, then fix it
                word = re.sub(r'[^a-z]+','',custom_regex)

                #fix typo
                if(word in dirt_arr):         
                    dirt_index = dirt_arr.index(word)
                    str_replace = clean_arr[dirt_index]
                    str_replace = str_replace+'-'+str_replace
                    new_text.append(str_replace)

                #not a typo
                else:
                    new_text.append(word+'-'+word)
            
            #not contain aposthrope and number
            else:                
                if(word in dirt_arr):
                    dirt_index = dirt_arr.index(word)
                    str_replace = clean_arr[dirt_index]
                    #regex handling (ktm nya, baka nya, etc)
                    match_regex = re.findall(r'[\w]+[\s]nya', str_replace)
                    for match_word in match_regex:
                        remove_whitespace = re.sub("nya", "", match_word)
                        str_replace = re.sub(match_word, remove_whitespace, str_replace)
                    new_text.append(str_replace)
                else:
                    new_text.append(word)
            
        text = " ".join(new_text)

        #regex normalization (di cek)
        for item in regex_arr:
            match_regex = re.search(r'\b'+item, text)
            if(match_regex):
                word = match_regex.group()
                #delete whitespace to concate word
                remove_whitespace = re.sub(r'\W', "", word)
                text = re.sub(word, remove_whitespace, text)

        #regex normalization (...2nya / ...2an)

        
        #regex normalization (... nya)
        match_regex = re.findall(r'[\w]+[\s]nya\b', text)
        for match_word in match_regex:
            remove_whitespace = re.sub(r"\W", "", match_word)
            text = re.sub(match_word, remove_whitespace, text)
        
        #regex for finding word end with aposthrope (orang")
        custom_regex = re.findall(r'[^"\s]*[a-z]+["]', text)
        if(len(custom_regex) > 1):
            for i in range(0, len(custom_regex)):
                regex_word = str(custom_regex[i])
                punctuation = re.sub("(\\d|\\W)+","",regex_word)
                str_replace = punctuation+'-'+punctuation
                str_regex_replace = re.sub(regex_word, str_replace, text)
                text = str_regex_replace
        
        elif(len(custom_regex) > 0):
            regex_word = str(custom_regex[0])
            punctuation = re.sub("(\\d|\\W)+","",regex_word)
            str_replace = punctuation+'-'+punctuation
            str_regex_replace = re.sub(regex_word, str_replace, text)
            text = str_regex_replace
        
        #regex for finding word end with number (orang2)
        custom_regex = re.findall(r'[^"\s]*[a-z]+[2]', text)
        if(len(custom_regex) > 1):
            for i in range(0, len(custom_regex)):
                #checking (handling P2, S2, etc)
                if(len(custom_regex[i]) > 2):
                    regex_word = str(custom_regex[i])
                    punctuation = re.sub("(\\d|\\W)+","",regex_word)
                    str_replace = punctuation+'-'+punctuation
                    str_regex_replace = re.sub(regex_word, str_replace, text)
                    text = str_regex_replace
        
        elif(len(custom_regex) > 0):
            if(len(custom_regex[0]) > 2):
                regex_word = str(custom_regex[0])
                punctuation = re.sub("(\\d|\\W)+","",regex_word)
                str_replace = punctuation+'-'+punctuation
                str_regex_replace = re.sub(regex_word, str_replace, text)
                text = str_regex_replace
        
        #regex normalization -- remove duplicated words in row (bla bla --> bla-bla)
        text = re.sub(r'\b(\w+)( \1\b)+', r'\1'+'-'+r'\1', text)

        return text
    
    #stopwords function
    def stopwordsRemoval(text):
        #list stopwords
        list_stopword =  set(stopwords.words('indonesian'))

        #filtering stopwords (handling negation words -- not removed)
        filter_stopword = set(["tidak", "kurang", "bukan","belum", "jangan", "cukup", "jauh", "lama"])
    
        #add stopwords
        add_stopword = []
        with open('Dataset/stopwords.txt', 'r') as f:
            content = f.read().splitlines()
            for item in content:
                add_stopword.append(item)

        #single word char
        for c in range(97, 122, 13):
            for c1 in range(c, c+13):
                add_stopword.append(chr(c1))
    
        #new list of stopwords
        new_list_stopword = [new_list_stopword 
                       for new_list_stopword in list_stopword if new_list_stopword not in filter_stopword]
        new_list_stopword.extend(add_stopword)
    
        arr_word_removed = []
        # tokens = word_tokenize(text)
    
        for word in text:
            if word not in new_list_stopword:
                arr_word_removed.append(word)
    
        return arr_word_removed

    #stemming function
    def stemmingText(text):
        #call module sastrawi
        factory_stem = StemmerFactory()
        stemmer = factory_stem.create_stemmer()

        #concat tokens into sentence for stemming process
        # text = concateText(text)

        #stemming process
        text = stemmer.stem(text)
    
        return text
        
    #concate text function
    def concateText(text):
        text_tokens = []
        for token in text:
            text_tokens.append(token)
        
        text = " ".join(text_tokens)

        return text
    
    def customFailedStemming(text):
        match_regex = re.findall(r'[\w]+nya\b', text)
        match_replace = re.findall(r'(\w+)nya\b', text)
        for i in range(0, len(match_regex)):
            text = re.sub(match_regex[i], match_replace[i], text)
        
        return text
    
    #==================================================================================================
    #parameter setting for text preprocessing
    #tuning: stopwords removal, stemming, n-grams (1-3)

    for i in range(0, len(arr_text)):
        #case folding
        text = caseFolding(arr_text[i])

        #option 1 -- complete (case folding, punctuation removal, normalization, stopwords removal)
        if(option == 1):
            #punctuation without aposthrope
            text = punctuationRemoval(text)

            #normalization process
            text = textNormalization(text)

            #stemming process
            text = stemmingText(text)
            text = customFailedStemming(text) #for english

            #stopword removal process
            text = tokenPunctuation(text)
            text = textTokenization(text)
            text = stopwordsRemoval(text)

            #concate text
            text = concateText(text)

        #option 2 -- case folding, punctuation removal, normalization, stopwords removal
        elif(option == 2):
            #punctuation without aposthrope
            text = punctuationRemoval(text)

            text = textNormalization(text)

            #handling word not stemmed
            text = punctuationRemoval(text)            
            
            #remove single char and digits
            text = tokenPunctuation(text)

            #stopwords removal process
            text = textTokenization(text)
            text = stopwordsRemoval(text)
            text = concateText(text)

        #option 3 -- case folding, punctuation removal, normalization, stemming
        elif(option == 3):
            #punctuation without aposthrope
            text = punctuationRemoval(text)

            #normalization process
            text = textNormalization(text)

            #stemming process
            text = stemmingText(text)
            text = customFailedStemming(text) #for english

            #stopword removal process
            text = tokenPunctuation(text)

        #========================================================
        #additional tuning preprocessing: normalization

        #option 4 -- case folding, punctuation removal
        elif(option == 4):
            #punctuation removal
            text = removePunctuation(text)
        
        #option 5 -- case folding, punctuation removal, normalization
        elif(option == 5):
            #punctuation without aposthrope
            text = removePunctuation(text)

            #normalization process
            text = textNormalization(text)
        
        arr_text[i] = text

    return arr_text

#save cleaned dataset after preprocessing
def save_clean_data(filename, arr_text, biro_unit):
    df = pd.DataFrame(data={"Teks_Aspirasi": arr_text, "Biro / Unit": biro_unit})
    df.to_csv(filename, sep=',',index=False)

#keyword extraction using tf-idf
def keywordExtraction(text_preprocess):
    from sklearn.feature_extraction.text import CountVectorizer
    from Sastrawi.StopWordRemover.StopWordRemoverFactory import StopWordRemoverFactory
    from sklearn.feature_extraction.text import TfidfTransformer
    import re

    #function to sort data in matrix
    def sortData(matrix):
        data = zip(matrix.col, matrix.data)
        result = sorted(data, key=lambda x: (x[1], x[0]), reverse=True)
        return result
    
    #function to calculate tfidf value
    def extractVector(feature_names, sorted_items, limit=10):
        #limit = tfidf value of top n item
        #get data only topn items from feature vector
        sorted_items = sorted_items[:limit]

        score_result = []
        feature_result = []

        # word index and corresponding tf-idf score
        for index, score in sorted_items:
            #keep track of feature name and its corresponding score
            score_result.append(round(score, 3))
            feature_result.append(feature_names[index])
        
        #result array (feature,score)
        results = {}
        for index in range(len(feature_result)):
            results[feature_result[index]] = score_result[index]
        
        return results
    
    def getDataKeywords(index):
        data = text_preprocess[index]
        tfidf_vect = transformer.transform(count_vectorizer.transform([data]))
        sorted_tfidf = sortData(tfidf_vect.tocoo())
        keyword_items = extractVector(feature_names,sorted_tfidf,10)

        return keyword_items
    
    def getKeywordResult(index, item):
        print("\nTeks Aspirasi: ", text_preprocess[index])
        print("\nKeywords:")
        for k in item:
            print(k,item[k])

    #---------------------------------------------------------------------------------------------
    #get text after preprocess
    data_preprocess = text_preprocess.tolist()

    #create vocabulary
    count_vectorizer = CountVectorizer(ngram_range=(1,2), max_df = 0.85) #mengabaikan kata yang muncul di 85% dokumen
    word_count_vector = count_vectorizer.fit_transform(data_preprocess)
    print(word_count_vector.shape)
    list(count_vectorizer.vocabulary_.keys())[:10]

    #get feature names
    transformer = TfidfTransformer(smooth_idf = False, use_idf = True, sublinear_tf=True)
    transformer.fit(word_count_vector)
    feature_names = count_vectorizer.get_feature_names()

    #get keyword extraction
    # for i in range(0, len(text_preprocess)):
    #     keyword = getDataKeywords(0)
    #     getKeywordResult(0, keyword)
    
    keyword = getDataKeywords(0)
    getKeywordResult(0, keyword)

#train test split function
def splitData(df):
    from sklearn.model_selection import train_test_split

    x = df['Teks_Aspirasi']
    y = df['Biro / Unit']

    #test size set to 0 because cross val used
    #stratify -- keep proportion of data distribution in the train and test dataset
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size = 0.3, random_state = 1)

    return x_train, x_test, y_train, y_test

def tuningMethodParams(df):
    from sklearn.model_selection import GridSearchCV
    from sklearn.model_selection import StratifiedKFold
    from sklearn.model_selection import StratifiedShuffleSplit
    from sklearn.metrics import classification_report, accuracy_score
    from sklearn.model_selection import cross_val_score
    from sklearn.pipeline import Pipeline
    from sklearn.svm import SVC
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.model_selection import RepeatedStratifiedKFold
    from sklearn.model_selection import train_test_split
    from pprint import pprint
    from time import time
    import logging

    logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s %(levelname)s %(message)s')
    
    #splitting dataset into train and test
    data_split = splitData(df)
    x_train = data_split[0]
    x_test = data_split[1]
    y_train = data_split[2]
    y_test = data_split[3]

    #using linear svc method
    clf_pipeline = Pipeline([
        ('tfidf', TfidfVectorizer(ngram_range=(1,1), smooth_idf=False, sublinear_tf=True)),
        ('clf', OneVsRestClassifier(SVC()))
    ])

    # clf_params = {
    #     'tfidf__max_df': (0.25, 0.5, 0.75, 1.0),
    #     'tfidf__ngram_range': ((1, 1), (1, 2), (1, 3)),
    #     'tfidf__norm': ('l1', 'l2'),
    #     'tfidf__use_idf': (True, False),
    #     'tfidf__smooth_idf': (True, False),
    #     'tfidf__sublinear_tf': (True, False),
    #     'clf__estimator__kernel': ('linear', 'poly', 'rbf'),
    #     'clf__estimator__C': (100, 10, 1, 0.1, 0.01, 0.001)
    # }
    
    # 'clf__estimator__gamma': [1, 0.1, 0.01, 0.0001],
    # clf_params = [
    #     {
    #         # 'tfidf__max_df': (0.25, 0.5, 0.75, 1),
    #         'tfidf__ngram_range': ((1, 1), (1, 2), (1, 3), (2,3), (2,2), (3,3)),
    #         # 'tfidf__ngram_range': ((1, 1)),
    #         'clf__estimator__kernel': ['linear', 'poly', 'rbf'],
    #         'clf__estimator__gamma': ['scale'],
    #         # 'clf__estimator__gamma': [100, 10, 1, 0.1, 0.01, 0.001, 0.0001],
    #         'clf__estimator__C': [100, 10, 1, 0.1, 0.01, 0.001, 0.0001]
    #     }
    # ]

    clf_params = [
        # 'tfidf__max_df': (0.25, 0.5, 0.75, 1),
        # 'tfidf__ngram_range': ((1, 1), (1, 2), (1, 3), (2,3), (2,2), (3,3)),
        # 'tfidf__ngram_range': ((1, 1)),
        # 'clf__estimator__kernel': ['linear', 'poly', 'rbf'],
        # 'clf__estimator__gamma': ['scale'],
        # 'clf__estimator__gamma': [100, 10, 1, 0.1, 0.01, 0.001, 0.0001],
        # 'clf__estimator__C': [100, 10, 1, 0.1, 0.01, 0.001, 0.0001]
        # {'clf__estimator__C': [1, 1.5, 2, 3, 4, 5], 'clf__estimator__kernel': ['linear']},
        {
            'clf__estimator__C': [100, 10, 1, 0.1, 0.01, 0.001, 0.0001], 
            # 'clf__estimator__gamma': ['scale', 'auto'], 
            'clf__estimator__kernel': ['linear'],
            'tfidf__max_df': (0.25, 0.5, 0.75, 1)
            # 'tfidf__ngram_range': ((1, 1), (1, 2), (1, 3), (2,3), (2,2), (3,3))
        }
    ]

    # cv = StratifiedShuffleSplit(n_splits = 5, test_size = 0.2, random_state = 0)
    # print('Performing Gridsearch for Parameter Tuning of Model Method\n')
    # t0 = time()
    # gcv = GridSearchCV(clf_pipeline, clf_params, scoring='accuracy', n_jobs=-1, cv=cv, verbose=1)
    # features = df['Teks_Aspirasi']
    # labels = df['Biro / Unit']
    # gcv.fit(features, labels)
    # print("done in %0.3fs" % (time() - t0))
    # print("Best combination: %f using %s" % (gcv.best_score_, gcv.best_params_))
    # print("\n")

    # for train_ind, test_ind in cv.split(features,labels):
    #     x_train, x_test = features[train_ind], features[test_ind]
    #     y_train, y_test = labels[train_ind],labels[test_ind]

    #     gcv.best_estimator_.fit(x_train,y_train)
    #     gcv.best_estimator_.predict(x_test)
    #     print('Train Accuracy: %.3f' % gcv.score(x_test, y_test))
    #     print('Test Accuracy: %.3f' % gcv.score(x_train, y_train))



    #model selection -- tuning model parameters
    print('Performing Gridsearch for Parameter Tuning of Model Method\n')
    t0 = time()
    cv = StratifiedKFold(n_splits=5, random_state=0).split(x_train, y_train)
    grid_search = GridSearchCV(clf_pipeline, clf_params, scoring='accuracy', n_jobs=-1, cv=cv, verbose=1)
    grid_search_result = grid_search.fit(x_train, y_train)
    print("done in %0.3fs" % (time() - t0))
    print("\n")

    print("Best combination: %f using %s" % (grid_search_result.best_score_, grid_search_result.best_params_))
    print("Combination Result:")
    print()
    means = grid_search_result.cv_results_['mean_test_score']
    stdevs = grid_search_result.cv_results_['std_test_score']
    params = grid_search_result.cv_results_['params']
    for mean, stdev, param in zip(means, stdevs, params):
        print("%f (%f) with: %r" % (mean, stdev, param))
    

    #training data with model selection
    svm = grid_search.best_estimator_
    svm.fit(x_train, y_train)
    y_pred = svm.predict(x_test)
    print('Train Accuracy: %.3f' % svm.score(x_test, y_test))
    print('Test Accuracy: %.3f' % svm.score(x_train, y_train))
    
    # from sklearn.metrics import classification_report, accuracy_score
    # print('Classifiaction Report \n')
    # print(classification_report(y_test, y_pred))

    #save model
    # saveModel(svm, 1)

    print('==============================================================\n')


def cobaTrainData(df):
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.svm import SVC
    from sklearn.svm import LinearSVC
    from sklearn.pipeline import Pipeline
    from sklearn import metrics
    from sklearn.metrics import classification_report, accuracy_score

    #splitting data into train and test sets
    data_split = splitData(df)
    x_train = data_split[0]
    x_test = data_split[1]
    y_train = data_split[2]
    y_test = data_split[3]

    #train and predict
    #transform list of dataset to tf_idf before passing to model

    # ('clf', OneVsRestClassifier(SVC(kernel='poly', C=100, gamma=0.01)))
    pipe_svm = Pipeline([
        ('tfidf_vect', TfidfVectorizer(max_df=0.5, ngram_range=(1,2), smooth_idf=False, sublinear_tf=True)),
        ('clf', OneVsRestClassifier(SVC(kernel='rbf', C=1000)))
    ])

    pipe_svm.fit(x_train, y_train)
    y_pred = pipe_svm.predict(x_test)
    print('Train Accuracy: %.15f' % pipe_svm.score(x_train, y_train))
    print('Test Accuracy: %.15f' % pipe_svm.score(x_test, y_test))

    #stratified k-fold for model performance
    from sklearn.model_selection import StratifiedKFold
    from sklearn.model_selection import cross_val_score

    cv = StratifiedKFold(n_splits=10).split(x_train, y_train)
    all_accuracies = cross_val_score(pipe_svm, x_train, y_train, cv=cv, n_jobs=-1)
    print('Cross Validation Accuracy: ', all_accuracies)
    print('Cross Validation Mean: ', all_accuracies.mean())
    print('==============================================================\n')

    # saveModel(pipe_svm, 1)

    # from sklearn.metrics import classification_report, accuracy_score
    # print('Classification Model Report \n')
    # print(classification_report(y_test, y_pred))

    # # confussion matrix
    # from sklearn.metrics import confusion_matrix
    # import matplotlib.pyplot as plt
    # import seaborn as sns

    # confusion = confusion_matrix(y_test, y_pred)
    # df_confusion = pd.DataFrame(confusion, pipe_svm.classes_, pipe_svm.classes_)
    
    # plt.figure(figsize=(15, 15))
    # heatmap = sns.heatmap(df_confusion, annot=True, fmt="d")
    # plt.title('Confusion Matrix Report with Accuracy is: {0:.3f}'.format(accuracy_score(y_test, y_pred)))
    # heatmap.yaxis.set_ticklabels(heatmap.yaxis.get_ticklabels(), rotation=0, ha='right', fontsize=8)
    # heatmap.xaxis.set_ticklabels(heatmap.xaxis.get_ticklabels(), rotation=45, ha='right', fontsize=8)
    # plt.ylabel('True Label (Actual)')
    # plt.xlabel('Predicted Label')
    # plt.show()


def rocAucScore(df):
    import matplotlib.pyplot as plt
    from sklearn.preprocessing import label_binarize
    from sklearn.metrics import roc_curve, auc
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.model_selection import train_test_split
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.svm import SVC
    from sklearn.pipeline import Pipeline

    x = df['Teks_Aspirasi']
    y = df['Biro / Unit']

    # Binarize the output
    y = label_binarize(y, classes=['baka', 'baak', 'uppk', 'ukk', 'kantin', 'perpustakaan', 'puskom', 'poliklinik'])
    n_classes = y.shape[1]

    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size = 0.2, random_state = 1, stratify=y)

    pipe_svm = Pipeline([
        ('tfidf_vect', TfidfVectorizer(ngram_range=(1,3), smooth_idf=False, sublinear_tf=True, max_df=0.25)),
        ('clf', OneVsRestClassifier(SVC(kernel='linear', C=0.0001)))
    ])
    y_score = pipe_svm.fit(x_train, y_train).decision_function(x_test)

    fpr = dict()
    tpr = dict()
    roc_auc = dict()
    for i in range(n_classes):
        fpr[i], tpr[i], _ = roc_curve(y_test[:, i], y_score[:, i])
        roc_auc[i] = auc(fpr[i], tpr[i])

    for i in range(n_classes):
        plt.figure()
        plt.plot(fpr[i], tpr[i], label='ROC curve (area = %0.2f)' % roc_auc[i])
        plt.plot([0, 1], [0, 1], 'k--')
        plt.xlim([0.0, 1.0])
        plt.ylim([0.0, 1.05])
        plt.xlabel('False Positive Rate')
        plt.ylabel('True Positive Rate')
        plt.title('Receiver operating characteristic example')
        plt.legend(loc="lower right")
        plt.show()


#coba training data tanpa tuning parameter
def trainingData(df):
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.svm import LinearSVC
    from sklearn.svm import SVC
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.pipeline import Pipeline
    from sklearn import metrics
    from sklearn.metrics import classification_report, accuracy_score

    #splitting data into train and test sets
    data_split = splitData(df)
    x_train = data_split[0]
    x_test = data_split[1]
    y_train = data_split[2]
    y_test = data_split[3]

    #function tuning params
    def tfidfTuning(x_train, y_train, params_name, params_value):
        #checking params name (ngramm max features, etc)
        if(params_name == "ngram"):    
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(sublinear_tf=True, max_df=0.25, smooth_idf=False, ngram_range=(1,2))),
                ('clf', OneVsRestClassifier(SVC(kernel='linear', C=(params_value))))
            ])
        
        elif(params_name == "max_df"):
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(ngram_range=(1,3), max_df=(params_value))),
                ('clf', OneVsRestClassifier(SVC(kernel='linear')))
            ])
        
        elif(params_name == "norm"):
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(ngram_range=(1,3), max_df=0.25, norm=(params_value))),
                ('clf', OneVsRestClassifier(SVC(kernel='linear')))
            ])
        
        elif(params_name == "use_idf"):
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(ngram_range=(1,3), max_df=0.25, norm=('l2'), 
                use_idf=(params_value) )),
                ('clf', OneVsRestClassifier(SVC(kernel='linear')))
            ])
        
        elif(params_name == "smooth_idf"):
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(use_idf=True, ngram_range=(1,3), max_df=0.25, norm=('l2'), 
                smooth_idf=(params_value) )),
                ('clf', OneVsRestClassifier(SVC(kernel='linear')))
            ])
        
        elif(params_name == "sublinear_tf"):
            pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(ngram_range=(1,3), max_df=0.25, norm=('l2'), 
                sublinear_tf=(params_value), smooth_idf=False, use_idf=False)),
                ('clf', OneVsRestClassifier(SVC(kernel='linear', C=0.001)))
            ])

        #training and model evaluation based on param selected
        pipe_svm.fit(x_train, y_train)
        y_pred = pipe_svm.predict(x_test)
        print('Train Accuracy: %.15f' % pipe_svm.score(x_train, y_train))
        print('Test Accuracy: %.15f' % pipe_svm.score(x_test, y_test))
        import numpy as np
        
        print('Test Accuracy Method: ', np.mean(y_pred == y_test))

        print("\n")
        
        # print('Classification Report \n')
        # print(classification_report(y_test, y_pred))

        #stratified k-fold for model performance
        from sklearn.model_selection import StratifiedKFold
        from sklearn.model_selection import cross_val_score
        import seaborn as sns
        from numpy import mean

        cv = StratifiedKFold(n_splits=5).split(x_train, y_train)
        all_accuracies = cross_val_score(pipe_svm, x_train, y_train, cv=cv, n_jobs=-1)
        print('Cross Validation Accuracy: ', all_accuracies)
        print('Cross Validation Mean: ', all_accuracies.mean())

        return all_accuracies.mean()

    
    def runTfidfTuning(params_name):
        #checking selected params want to tuning
        #ngram
        if(params_name == 'ngram'):
            #arr params and scores
            # tfidf_params = [(1,1), (1,2), (1,3), (2,3), (2,2), (3,3)]
            tfidf_params = [0.00001, 0.0001, 0.001, 0.01, 0.1, 1, 10, 100, 1000, 5000]
            tfidf_scores = []
        #max_df
        elif(params_name == 'max_df'):
            #arr params and scores
            tfidf_params = [0.25, 0.5, 0.75, 1.0]
            tfidf_scores = []
        #max_df
        elif(params_name == 'norm'):
            #arr params and scores
            tfidf_params = ['l1', 'l2']
            tfidf_scores = []
        #use_idf
        elif(params_name == 'use_idf'):
            #arr params and scores
            tfidf_params = [True, False]
            tfidf_scores = []
        #smooth_idf
        elif(params_name == 'smooth_idf'):
            #arr params and scores
            tfidf_params = [True, False]
            tfidf_scores = []
        #sublinear_tf
        elif(params_name == 'sublinear_tf'):
            #arr params and scores
            tfidf_params = [True, False]
            tfidf_scores = []
            

        #tuning ngram range for tfidf
        for i in tfidf_params:
            tfidf_scores.append(tfidfTuning(x_train, y_train, params_name, i))
        
        #get scores
        for i in tfidf_scores:
            print(i)
        
        #print graphic evaluation accuracy
        if(params_name == 'ngram'):
            drawGraphicEvaluation('ngram', tfidf_scores)
        #max_df
        elif(params_name == 'max_df'):
            drawGraphicEvaluation('max_df', tfidf_scores)
        #max_df
        elif(params_name == 'norm'):
            drawGraphicEvaluation('norm', tfidf_scores)
        #use_idf
        elif(params_name == 'use_idf'):
            drawGraphicEvaluation('use_idf', tfidf_scores)
        #smooth_idf
        elif(params_name == 'smooth_idf'):
            drawGraphicEvaluation('smooth_idf', tfidf_scores)
        #sublinear_tf
        elif(params_name == 'sublinear_tf'):
            drawGraphicEvaluation('sublinear_tf', tfidf_scores)
    
    def drawGraphicEvaluation(param_name, param_score):
        if(param_name=='ngram'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            # plt.plot(['(1,1)', '(1,2)', '(1,3)', '(2, 3)', '(2,2)', '(3,3)'], param_score)
            plt.plot(['0.00001', '0.0001', '0.001', '0.01', '0.1', '1', '10', '100', '1000', '5000'], param_score)
            plt.xlabel('Parameter (C)')
            plt.ylabel('Akurasi')
            plt.show()

        elif(param_name=='max_df'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            plt.plot(['0.25', '0.5', '0.75', '1.0'], param_score)
            plt.xlabel('Parameter (max_df)')
            plt.ylabel('Akurasi')
            plt.show()
        
        elif(param_name=='norm'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            plt.plot(['l1', 'l2'], param_score)
            plt.xlabel('Parameter (norm)')
            plt.ylabel('Akurasi')
            plt.show()
        
        elif(param_name=='use_idf'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            plt.plot(['True', 'False'], param_score)
            plt.xlabel('Parameter (use_idf)')
            plt.ylabel('Akurasi')
            plt.show()

        elif(param_name=='smooth_idf'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            plt.plot(['True', 'False'], param_score)
            plt.xlabel('Parameter (smooth_idf)')
            plt.ylabel('Akurasi')
            plt.show()
        
        elif(param_name=='sublinear_tf'):
            #print graphic evaluation accuracy
            from matplotlib import pyplot as plt
            plt.title('Parameter TfIdf Tuning')
            plt.plot(['True', 'False'], param_score)
            plt.xlabel('Parameter (sublinear_tf)')
            plt.ylabel('Akurasi')
            plt.show()
        

    #run method tuning tfidf
    # param=['ngram', 'max_df', 'norm', 'use_idf', 'smooth_idf', 'sublinear_tf', 'max_features']
    param=['ngram']
    for item in param:
        runTfidfTuning(item)
    
    # #training data using linearSVC()
    # if(option == 0):
    #     pipe_svm = Pipeline([
    #         ('tfidf_vect', TfidfVectorizer()),
    #         ('clf', LinearSVC(C=0.001, penalty='l1', dual=False))
    #     ])

    #     pipe_svm.fit(x_train, y_train)
    #     y_pred = pipe_svm.predict(x_test)
    #     print('Test Accuracy: %.3f' % pipe_svm.score(x_test, y_test))

    #     import numpy as np
    #     print('Test Accuracy Method: ', np.mean(y_pred == y_test))

        
    #     print('Classification Report \n')
    #     print(classification_report(y_test, y_pred))

    #     #stratified k-fold for model performance
    #     from sklearn.model_selection import StratifiedKFold
    #     from sklearn.model_selection import cross_val_score
    #     import seaborn as sns
    #     from numpy import mean

    #     cv = StratifiedKFold(n_splits=10).split(x_train, y_train)
    #     all_accuracies = cross_val_score(pipe_svm, x_train, y_train, cv=cv, n_jobs=-1)
    #     print('Cross Validation Accuracy: ', all_accuracies)
    #     print('Cross Validation Mean: ', all_accuracies.mean())

    #     #confussion matrix
    #     from sklearn.metrics import confusion_matrix
    #     import matplotlib.pyplot as plt

    #     confusion = confusion_matrix(y_test, y_pred)
    #     df_confusion = pd.DataFrame(confusion, pipe_svm.classes_, pipe_svm.classes_)
        
    #     plt.figure(figsize=(15, 15))
    #     heatmap = sns.heatmap(df_confusion, annot=True, fmt="d")
    #     plt.title('Confusion Matrix Report with Accuracy is: {0:.3f}'.format(accuracy_score(y_test, y_pred)))
    #     heatmap.yaxis.set_ticklabels(heatmap.yaxis.get_ticklabels(), rotation=0, ha='right', fontsize=8)
    #     heatmap.xaxis.set_ticklabels(heatmap.xaxis.get_ticklabels(), rotation=45, ha='right', fontsize=8)
    #     plt.ylabel('True Label (Actual)')
    #     plt.xlabel('Predicted Label')
    #     plt.show()

    #training data using SVC()
    # elif(option == 1){

    # }

def valiationCurve(df):
    import matplotlib.pyplot as plt
    import numpy as np

    from sklearn.datasets import load_digits
    from sklearn.multiclass import OneVsRestClassifier
    from sklearn.svm import SVC
    from sklearn.svm import LinearSVC
    from sklearn.model_selection import validation_curve
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.feature_extraction.text import CountVectorizer
    from sklearn.feature_extraction.text import TfidfTransformer
    from sklearn.pipeline import Pipeline
    from sklearn.model_selection import StratifiedKFold

    #split data
    data_split = splitData(df)
    x_train = data_split[0]
    x_test = data_split[1]
    y_train = data_split[2]
    y_test = data_split[3]

    # count_vect = CountVectorizer()
    # X_train_counts = count_vect.fit_transform(x_train)
    # tf_transformer = TfidfTransformer().fit(X_train_counts)
    # X_train_tf = tf_transformer.transform(X_train_counts)

    tfidf_vectorizer = TfidfVectorizer(ngram_range=(1,2), norm='l2', smooth_idf=False, max_df=0.25, sublinear_tf=True)
    X_tfidf_train = tfidf_vectorizer.fit_transform(x_train)
    X_tfidf_test = tfidf_vectorizer.transform(x_test)

    pipe_svm = Pipeline([
                ('tfidf_vect', TfidfVectorizer(ngram_range=(1,2), norm='l2', smooth_idf=False, sublinear_tf=True, max_df=0.25)),
                ('clf', OneVsRestClassifier(SVC(kernel='linear')))
            ])

    # estimator = OneVsRestClassifier(SVC())
    # print(estimator.get_params().keys())

    # param_range = np.logspace(0, 1, 10)
    # C=100, gamma=0.01
    # param_range = np.arange(1, 10, 2)
    param_range = [0.0001, 0.001, 0.01, 0.1, 1.0, 10.0, 100.0]
    cv = StratifiedKFold(n_splits=5).split(X_tfidf_train, y_train)
    # train_scores, test_scores = validation_curve(
    #     SVC(kernel='poly', C=10, gamma=0.01), X_tfidf_train, y_train, param_name="degree", param_range=param_range,
    #     scoring="accuracy", n_jobs=1, cv=cv)
    train_scores, test_scores = validation_curve(pipe_svm, x_train, y_train, param_name="clf__estimator__C", param_range=param_range, scoring="accuracy", n_jobs=1, cv=5)
    train_scores_mean = np.mean(train_scores, axis=1)
    train_scores_std = np.std(train_scores, axis=1)
    test_scores_mean = np.mean(test_scores, axis=1)
    test_scores_std = np.std(test_scores, axis=1)

    # Plot mean accuracy scores for training and test sets
    plt.subplots(1, figsize=(7,7))
    plt.plot(param_range, train_scores_mean, label="Training score", color="black", marker='o', markersize=5)
    plt.plot(param_range, test_scores_mean, label="Cross-validation score", color="dimgrey", marker='o', markersize=5)

    # Plot accurancy bands for training and test sets
    plt.fill_between(param_range, train_scores_mean - train_scores_std, train_scores_mean + train_scores_std, color="darkorange", alpha=0.15)
    plt.fill_between(param_range, test_scores_mean - test_scores_std, test_scores_mean + test_scores_std, color="blue", alpha=0.15)

    # Create plot    
    plt.grid()
    plt.xscale('log')
    plt.ylim([0.8, 1.0])
    plt.title("Validation Curve")
    plt.xlabel("C")
    plt.ylabel("Accuracy Score")
    plt.legend(loc="best")
    plt.show()



def testData(text):
    from sklearn.externals import joblib

    #import model
    filename = 'D:/Kuliah/Semester_8/Skripsi/Training Model/Model/model_v1.pkl'
    model = joblib.load(filename)
    result = model.predict([text])[0]
    print(result)

    return "result"
    # print(clf.predict(tfidf.transform(text)))

def saveModel(model, option):
    from sklearn.externals import joblib

    if(option==1):
        filename = 'Model/model_v1.pkl'
    elif(option==2):
        filename = 'Model/model_v2.pkl'
    elif(option==3):
        filename = 'Model/model_v3.pkl'
    
    joblib.dump(model, filename)



def getFeaturesData(text_preprocess, option):
    from sklearn.feature_extraction.text import TfidfTransformer
    from sklearn.feature_extraction.text import CountVectorizer

    #create a vocabulary of features words
    #ignore features appear 85% of dataset
    cv = CountVectorizer(max_df = 0.85)
    word_count_vector = cv.fit_transform(text_preprocess)
    print(word_count_vector.shape)

    #idf value calculation using TFIDFtransformer
    tfidf_trf = TfidfTransformer()
    tfidf_trf.fit(word_count_vector)
    df_idf = pd.DataFrame(tfidf_trf.idf_, index=cv.get_feature_names(),columns=["idf_score"])
    df_idf.sort_values(by=['idf_score'])

    #tf-idf value calculation using TFIDFtransformer
    count_vector = cv.transform(text_preprocess)
    tf_idf = tfidf_trf.transform(count_vector)
    feature_name = cv.get_feature_names()
    #0 -- index dokumen
    first_document_vector=tf_idf[0]
    df_tfidf_trf = pd.DataFrame(first_document_vector.T.todense(), index=feature_name, columns=["tf_idf_score"])
    df_tfidf_trf.sort_values(by=["tf_idf_score"],ascending=False)
    print(df_tfidf_trf)

    filename = None
    if(option == 1):
        filename = 'Dataset/features_1.txt'
    elif(option == 2):
        filename = 'Dataset/features_2.txt'
    elif(option == 3):
        filename = 'Dataset/features_3.txt'
    elif(option == 4):
        filename = 'Dataset/features_4.txt'
    else:
        filename = 'Dataset/features_5.txt'

    with open(filename, 'w') as f:
        for i in range(0, len(feature_name)):
            # feature_name[i] += ',  ' + str(df_tfidf_trf['tf_idf_score'][i])
            f.write("%s\n" % feature_name[i])


def tfIdfExtraction(df):
    from sklearn.feature_extraction.text import CountVectorizer
    from sklearn.feature_extraction.text import TfidfTransformer
    from sklearn.feature_extraction.text import TfidfVectorizer
    from nltk.corpus import stopwords 

    txt1 = df
    
    # Getting ngram
    vectorizer = CountVectorizer(ngram_range =(2, 2))
    X1 = vectorizer.fit_transform(txt1) 
    features = (vectorizer.get_feature_names()) 
    print("\n\nFeatures : \n", features)
    print("\n\nX1 : \n", X1.toarray()) 

    # Applying TFIDF 
    # You can still get n-grams here 
    list_stopword =  set(stopwords.words('indonesian'))
    vectorizer = TfidfVectorizer(ngram_range = (2, 2), max_df=0.85, sublinear_tf=True, smooth_idf=False) 
    X2 = vectorizer.fit_transform(txt1) 
    scores = (X2.toarray()) 
    print("\n\nScores : \n", scores)

    # Getting top ranking features 
    sums = X2.sum(axis = 0) 
    data1 = [] 
    for col, term in enumerate(features): 
        data1.append( (term, sums[0, col] )) 
    ranking = pd.DataFrame(data1, columns = ['term', 'rank']) 
    words = (ranking.sort_values('rank', ascending = False)) 
    print ("\n\nWords : \n", words.head(30)) 



#++++++++++++++++++++++++++++++ START CODE HERE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++

#data preprocess function
def dataPreprocess():
    #load and create dataframe from original dataset
    filename = 'Dataset/Dataset_Skripsi_Original.csv'
    df = load_dataset(filename)
    # load_distribution_graphic(df)
    get_class_distribution(df)

    #temp variable for text preprocessing
    text_preprocess_v1 = df['Teks_Aspirasi'].copy()
    class_label = df['Biro / Unit'].copy()
    text_preprocess_v2 = df['Teks_Aspirasi'].copy()
    text_preprocess_v3 = df['Teks_Aspirasi'].copy()
    text_preprocess_v4 = df['Teks_Aspirasi'].copy()
    text_preprocess_v5 = df['Teks_Aspirasi'].copy()

    # tuning  preprocessing
    from pprint import pprint
    from time import time
    import logging

    logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s %(levelname)s %(message)s')
    
    print("\n")
    
    t0 = time()
    text_preprocess_v1 = clean_text(text_preprocess_v1, 1) #version 1 -- stopwords removal and stemming
    print("Preprocessing version 1 done in %0.3fs" % (time() - t0))
    
    t0 = time()
    text_preprocess_v2 = clean_text(text_preprocess_v2, 2) #version 2 -- only stopwords removal
    print("Preprocessing version 2 done in %0.3fs" % (time() - t0))

    t0 = time()
    text_preprocess_v3 = clean_text(text_preprocess_v3, 3) #version 3 -- only stemming
    print("Preprocessing version 3 done in %0.3fs" % (time() - t0))


    t0 = time()
    text_preprocess_v4 = clean_text(text_preprocess_v4, 4) #version 4 -- case folding and punct
    print("Preprocessing version 4 done in %0.3fs" % (time() - t0))

    t0 = time()
    text_preprocess_v5 = clean_text(text_preprocess_v5, 5) #version 5 -- case folding, punct, normalization
    print("Preprocessing version 5 done in %0.3fs" % (time() - t0))

    # # save clean dateset to file
    save_clean_data('Dataset/Dataset_Skripsi_Cleaned (Version 1).csv', text_preprocess_v1, class_label)
    save_clean_data('Dataset/Dataset_Skripsi_Cleaned (Version 2).csv', text_preprocess_v2, class_label)
    save_clean_data('Dataset/Dataset_Skripsi_Cleaned (Version 3).csv', text_preprocess_v3, class_label)
    save_clean_data('Dataset/Dataset_Skripsi_Cleaned (Version 4).csv', text_preprocess_v4, class_label)
    save_clean_data('Dataset/Dataset_Skripsi_Cleaned (Version 5).csv', text_preprocess_v5, class_label)

    print("\n")

    #keyword extraction using tf-idf
    # index = 0
    # keyword = getDataKeywords(index)
    # getKeywordResult(index,keyword)
    # keywordExtraction(text_preprocess_v1)

    #get features data
    getFeaturesData(text_preprocess_v1, 1)
    getFeaturesData(text_preprocess_v2, 2)
    getFeaturesData(text_preprocess_v3, 3)
    getFeaturesData(text_preprocess_v4, 4)
    getFeaturesData(text_preprocess_v5, 5)


# df['Teks_Aspirasi'] = text_preprocess_v2
# c = ['suasana yang nyaman sangat mendukung saya untuk mengerjakan skripsi sambil baca buku']
# trainData(df, c)

#preprocess data
# dataPreprocess()

#load dataset cleaned
df_v1 = load_dataset('Dataset/Dataset_Skripsi_Cleaned (Version 1).csv')
# df_v2 = load_dataset('Dataset/Dataset_Skripsi_Cleaned (Version 2).csv')
# df_v3 = load_dataset('Dataset/Dataset_Skripsi_Cleaned (Version 3).csv')
# df_v4 = load_dataset('Dataset/Dataset_Skripsi_Cleaned (Version 4).csv')
# df_v5 = load_dataset('Dataset/Dataset_Skripsi_Cleaned (Version 5).csv')

# from pprint import pprint
# from time import time
# import logging

# logging.basicConfig(level=logging.INFO,
#                 format='%(asctime)s %(levelname)s %(message)s')
# time_test_preprocess = time()
trainingData(df_v1)
# cobaTrainData(df_v1)
# print("Preprocessing version 1 done in %0.3fs" % (time() - time_test_preprocess))
# # valiationCurve(df_v1)

# valiationCurve(df_v1)
# tuningMethodParams(df_v1)
# testData("Layanan internet")

# tfIdfExtraction(df_v1['Teks_Aspirasi'])

# rocAucScore(df_v1)


#splitting data
# data_split = splitData(df_v1)

#coba train data
# cobaTrainData(data_split[0], data_split[1], data_split[2], data_split[3], 1)
# print('-------------------------------\n')