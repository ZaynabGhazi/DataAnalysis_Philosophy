import pandas as pd
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
import argparse
import sys
import matplotlib
import numpy as np
from sklearn.linear_model import LinearRegression
import scipy.stats as stats

zipf_table = str(sys.argv[1])
zipf_file = pd.read_csv(zipf_table,header=None,names=['freq'],engine='python')
zipf_freq=np.array(zipf_file.freq)
zipf_freq[::-1].sort()
zipf_rank = np.linspace(1,len(zipf_freq),len(zipf_freq),endpoint=True)
fig,ax=plt.subplots(1,1)
plt.loglog(zipf_rank,zipf_freq,basex=10,basey=10)
plt.xlabel("Word Rank")
plt.ylabel("Word Frequency")
def exp_func(x,a,b):
    return a*np.power(x,b)
popt,pcov=curve_fit(exp_func,zipf_rank,zipf_freq)
plt.loglog(zipf_rank,exp_func(zipf_rank,*popt),'r-')
#Residual analysis:
chi = (((zipf_freq-exp_func(zipf_rank,*popt))**2)/exp_func(zipf_rank,*popt)).sum()
print("chi2 is ",chi)
crit = stats.chi2.ppf(q = 0.95,df = len(zipf_freq)-2)
p_value = 1 - stats.chi2.cdf(x=chi, df=len(zipf_freq)-2)
print(crit,p_value)
plt.show()
