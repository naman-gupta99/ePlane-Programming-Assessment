#include<bits/stdc++.h>

using namespace std;

// Swap to pointers
void swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

class MinHeap {
        int *heap_arr;
        int capacity;
        int heap_size;
    public:
        // Constructor
        MinHeap(int cap) {
            heap_size = 0;
            capacity = cap;
            heap_arr = new int[cap];
        }

        int parent(int i) {
            return (i-1)/2;
        }

        int left(int i) {
            return (2*i +1);
        }

        int right(int i) {
            return (2*i +2);
        }

        // MinHeapify sub tree
        void MinHeapify(int i) {
            int l = left(i);
            int r = right(i);
            int smallest = i;
            if (l < heap_size && heap_arr[l] < heap_arr[i]) {
                smallest = l;
            }
            if (r < heap_size && heap_arr[r] < heap_arr[i]) {
                smallest = r;
            }
            if (smallest != i) {
                swap(&heap_arr[i], &heap_arr[smallest]);
                MinHeapify(smallest);
            }
        }

        // Insert a new key
        void insertKey(int k) {
            if (heap_size == capacity) {
                cout << "Heap Overflowed";
            }

            heap_size++;
            int i = heap_size - 1;
            heap_arr[i] = k;

            while(i != 0 && heap_arr[parent(i)] > heap_arr[i]) {
                swap(&heap_arr[i], &heap_arr[parent(i)]);
                i = parent(i);
            }
        }

        // Get the minimum value in the heap
        int getMin() {
            return heap_arr[0];
        }
        
        // Remove the minimum value from the heap
        int extractMin() {
            if (heap_size <= 0) {
                return INT_MAX;
            }

            if (heap_size == 1) {
                heap_size--;
                return heap_arr[0];
            }

            int root = heap_arr[0];
            heap_arr[0] = heap_arr[heap_size - 1];
            heap_size--;
            MinHeapify(0);

            return root;
        }

        // Decrease the value of an index
        void decrease_key(int i, int n) {
            heap_arr[i] = n;
            while (i != 0 && heap_arr[parent(i)] > heap_arr[i]) {
                swap(&heap_arr[parent(i)], &heap_arr[i]);
                i = parent(i);
            }
        }

        // Delete an element
        void deleteKey(int i) {
            decrease_key(i, INT_MIN);
            extractMin();
        }

};

int main() {

    int capacity, choice;

    cout<<"Enter Capacity of the Heap: ";
    cin>>capacity;

    MinHeap heap(capacity);

    while (true) {
        cout<<"Choices: \n1. Insert Key \n2. Extract Min \n3. Get Min \n4. Decrease Key \n5. Exit"<<endl;
        cout<<"Enter Choice: ";
        cin>>choice;

        switch (choice) {
            case 1:
                int key;
                cout<<"Insert Key Value: ";
                cin>>key;
                heap.insertKey(key);
                break;
            case 2:
                cout<<"Extracted Value in Heap: "<<heap.extractMin()<<endl;
                break;
            case 3:
                cout<<"Minimum Value in Heap: "<<heap.getMin()<<endl;
                break;
            case 4:
                int index, new_val;
                cout<<"Enter Index of value to decrease and the new value: ";
                cin>>index>>new_val;
                heap.decrease_key(index, new_val);
                break;
            case 5:
                return 1;
                break;
    
            default:
                cout<<"Invalid Choice";
        };
    }
}
