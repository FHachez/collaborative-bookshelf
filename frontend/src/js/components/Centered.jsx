import React from 'react';
import PropTypes from 'prop-types';


export default function Centered(props) {
  const styling = {
    vertically: {
      flex: '0 0 auto',
      height: '100%',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
    horizontally: {
      height: 'auto',
      width: '50%',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
  };

  return (
    <div style={styling.vertically} >
      <div style={styling.horizontally} >
        {props.children}
      </div>
    </div>
  );
}

Centered.propTypes = {
  children: PropTypes.element.isRequired,
};

