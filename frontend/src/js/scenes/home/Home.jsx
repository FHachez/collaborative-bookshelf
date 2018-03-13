import React from 'react';
import PropTypes from 'prop-types';

import Tiles from 'grommet/components/Tiles';
import Tile from 'grommet/components/Tile';
import Image from 'grommet/components/Image';
import Box from 'grommet/components/Box';

import medicine from './assets/medicine.png';

const handleOnClick = (history, id) => (
  () => {
    if (id === 'medicines') {
      history.push('/medicines');
    }
  }
);

const Home = props => (
  <Box
    pad='medium'
    full
    colorIndex='light-2'
  >
    <Tiles
      selectable
      flush={false}
      fill={false}
    >

      <Tile
        onClick={handleOnClick(props.history, 'medicines')}
      >
        <Image
          src={medicine}
          caption='Medicines'
          size='small'
        />
      </Tile>
    </Tiles>
  </Box>
);

export default Home;

Home.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func
  }).isRequired
};
