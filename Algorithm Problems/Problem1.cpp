#include <bits/stdc++.h>
#include <pthread.h>

using namespace std;

struct range_info {
    int start;
    int end;
    int* a;
};

map<int, int> mp;
pthread_mutex_t lock_1;

void* func(void *arg) {
    pthread_mutex_lock(&lock_1);
    
    struct range_info* rptr = (struct range_info*) arg;

    for (int i=rptr->start; i <= rptr->end; i++) {
        map<int, int>::iterator it;

        it = mp.find(rptr->a[i]);

        if (it == mp.end()) {
            mp.insert({rptr->a[i], 1});
        } else {
            it->second++;
        }
    }

    pthread_mutex_unlock(&lock_1);
    return NULL;
}

void uniqueNumbers(int arr[], int N, int T) {
  pthread_t threads[T];
  int start = 0;
  int end = start + (N/T) -1;
  
  for (int i = 0; i<T; i++) {

    struct range_info* rptr = (struct range_info*)malloc(sizeof(struct range_info));

    rptr->start = start;
    rptr->end = end;
    rptr->a = arr;

    int a = pthread_create(&threads[i], NULL, func, (void*)(rptr));
    start = end + 1;
    end = start + (N/T) -1;
    if (i == T-2) {
      end = N - 1;
    }
  }

  for (int i = 0; i < T; i++) {
        int rc 
        = pthread_join(threads[i], NULL);
    }

    for (auto it: mp) {
                  
                                           
        if (it.second == 1) {
            cout << it.first << " ";
        }
    }
}

int main()
{
    int N, T;
    int arr[1000];
    cout<<"Enter length of Array and Number of Threads: "<<endl;
    cin>>N>>T;
    cout<<"Enter elements of array: "<<endl;
  for (int i=0; i<N; i++) {
    cin>>arr[i];
  }
  uniqueNumbers(arr, N, T);
}