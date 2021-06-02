# include <bits/stdc++.h>
using namespace std;

int calculateNumberOfKills(int arr[], int N, int K) {
  
    bool prime[K + 1];
    memset(prime, true, sizeof(prime));
    sort(arr, arr + N);

    for (int i=0; i < N; i++) {
        if (arr[i] > K) {
            break;
        }

        if (prime[arr[i]] and arr[i] != 0) {
            
            for (int j = arr[i]; j <= K; j+=arr[i]) {
                prime[j] = false;
            }

        }
    }
    int res = 0;
    for (int i=1; i<=K; i++) {
        if (!prime[i]) {
            res += 1;
        }
    }
    return res;
}

int main() {
  int N, K;
  cout<<"Enter the number of Kids and the number of Monsters:"<<endl;
  cin>>N>>K;
  cout<<"Enter the powers of kids' guns:"<<endl;
  int *arr = new int(N);
  for (int i=0; i< N; i++) {
      cin>>arr[i];
  }
  cout<<"The maximum number of Monsters killed: "<<calculateNumberOfKills(arr, N, K);
}
