import * as React from 'react';

import { Button, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import DatarizeLoggerViewManager from 'react-native-datarize-logger';

export default function App() {
  return (
    <View style={styles.container}>
      <DatarizeLoggerViewManager color="#32a852" style={styles.box}>
        <Button title={'Press me'} onPress={() => {}} />
        <TouchableOpacity>
          <Text>PRess me 2</Text>
        </TouchableOpacity>
      </DatarizeLoggerViewManager>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 200,
    height: 200,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
